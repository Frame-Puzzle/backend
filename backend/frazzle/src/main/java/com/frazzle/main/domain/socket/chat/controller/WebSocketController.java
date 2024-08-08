package com.frazzle.main.domain.chat.controller;


import com.frazzle.main.domain.socket.chat.dto.SendMessageDto;
import com.frazzle.main.domain.socket.chat.service.ChatService;
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

@RestController
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final UserRepository userRepository;

    //채팅 보내는 메서드
    @MessageMapping("/message/{boardId}")
    public void sendMessage(
            @DestinationVariable int boardId,
            SendMessageDto sendMessage,
            SimpMessageHeaderAccessor accessor) {

        //이메일로부터 사용자 찾기
        String email = (String) accessor.getSessionAttributes().get("senderEmail");
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        sendMessage.changeNickname(user.getNickname());

        log.info(sendMessage.toString());

        simpMessagingTemplate.convertAndSend("/sub/message/" + boardId, sendMessage);
    }

}
