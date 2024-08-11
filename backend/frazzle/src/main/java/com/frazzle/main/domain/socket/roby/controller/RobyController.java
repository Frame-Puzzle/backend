package com.frazzle.main.domain.socket.roby.controller;

import com.frazzle.main.domain.socket.chat.dto.SendMessageDto;
import com.frazzle.main.domain.socket.chat.service.ChatService;
import com.frazzle.main.domain.socket.roby.entity.Roby;
import com.frazzle.main.domain.socket.roby.entity.RobyNotification;
import com.frazzle.main.domain.socket.roby.entity.RobyUser;
import com.frazzle.main.domain.socket.roby.service.RobyService;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RobyController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RobyService robyService;
    private final ChatService chatService;
    private final UserRepository userRepository;
    private final Map<Integer, ScheduledFuture<?>> timers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    //대기방 입장
    @MessageMapping("/roby/entry/{boardId}")
    public void entryChat(
            @DestinationVariable int boardId,
            SendMessageDto sendMessageDto,
            SimpMessageHeaderAccessor accessor) {

        //jwt로 부터 이메일을 얻어 유저를 얻음
        String email = (String) accessor.getSessionAttributes().get("senderEmail");

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        //대기방에서 유저의 정보
        RobyUser robyUser = RobyUser.createRoomUser(user.getUserId(), user.getNickname(), user.getProfileImg());

        //퍼즐판의 id를 통해 대기방을 만들거고 유저 추가
        robyService.addUserToRoby(boardId, robyUser);

        //보드id를 통해 대기방 정보 찾기
        Roby roby = robyService.getRoby(boardId);

        log.info("entryChat roomId={}, roby={}", boardId, roby);

        //알림 추가
        RobyNotification notification = RobyNotification.createRoomNotification("입장", roby);

        // /sub/room으로 메시지 보내기
        simpMessagingTemplate.convertAndSend("/sub/roby/" + boardId, notification);

        //채팅에 접속
        sendMessageDto = chatService.entryChat(robyUser, boardId, sendMessageDto);

        log.info("message: " + sendMessageDto.getMessage());
        log.info("user.getUserId: "+user.getUserId());

        // /sub/message를 구독한 메서드에 메시지 보냄
        simpMessagingTemplate.convertAndSend("/sub/message/" + boardId, sendMessageDto);

        // 타이머 시작 (이미 존재하면 시작하지 않음)
        startTimer(boardId);
    }

    //대기방 나가기
    @MessageMapping("/roby/exit/{boardId}")
    public void exitChat(
            @DestinationVariable int boardId,
            SendMessageDto sendMessageDto,
            SimpMessageHeaderAccessor accessor) {

        log.info("exitChat roomId={}, sendMessageDto={}", boardId, sendMessageDto);
        log.info(sendMessageDto.toString());

        //jwt로 부터 이메일을 얻어 유저를 얻음
        String email = (String) accessor.getSessionAttributes().get("senderEmail");

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        RobyUser robyUser = RobyUser.createRoomUser(user.getUserId(), user.getNickname(), user.getProfileImg());
        //유저 제거
        robyService.removeUserFromRoby(boardId, robyUser);

        //대기방에 메시지 보내기
        Roby roby = robyService.getRoby(boardId);
        RobyNotification notification = RobyNotification.createRoomNotification("퇴장", roby);
        simpMessagingTemplate.convertAndSend("/sub/roby/" + boardId, notification);

        log.info("message: " + sendMessageDto.getMessage());
        log.info("user.getUserId: "+user.getUserId());

        // /sub/message를 구독한 메서드에 메시지 보냄
        simpMessagingTemplate.convertAndSend("/sub/message/" + boardId, sendMessageDto);

        chatService.exitChat(robyUser, boardId, sendMessageDto);

        // 타이머 종료 (모든 사용자가 나간 경우)
        if (robyService.isEmpty(boardId)) {
            stopTimer(boardId);
        }
    }

    //타이머 시작
    private void startTimer(int boardId) {
        if (timers.containsKey(boardId)) {
            // 이미 타이머가 실행 중인 경우
            log.info("Timer already running for boarId={}", boardId);
            return;
        }

        final long[] remainingTime = {300}; // 10분 = 600초

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            if (remainingTime[0] > 0) {
                remainingTime[0]--;
                simpMessagingTemplate.convertAndSend("/sub/roby/timer/" + boardId, remainingTime[0]);
            }
            if (remainingTime[0] <= 0) {
                // 타이머 종료 시 필요한 작업 수행
                simpMessagingTemplate.convertAndSend("/sub/roby/timer/" + boardId, "타임아웃");
                stopTimer(boardId);
            }
        }, 0, 1, TimeUnit.SECONDS);

        timers.put(boardId, future);
        log.info("Timer started for roomId={}", boardId);
    }

    // 타이머 종료 메서드
    private void stopTimer(int boardId) {
        ScheduledFuture<?> future = timers.remove(boardId);
        if (future != null) {
            future.cancel(true);
        }
        simpMessagingTemplate.convertAndSend("/sub/roby/timer/" + boardId, "타이머가 중지되었습니다.");
    }
}
