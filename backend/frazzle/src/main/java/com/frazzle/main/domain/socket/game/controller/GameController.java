package com.frazzle.main.domain.socket.game.controller;

import com.frazzle.main.domain.socket.game.dto.MoveRequestDto;
import com.frazzle.main.domain.socket.game.dto.ReleaseRequestDto;
import com.frazzle.main.domain.socket.game.dto.StartRequestDto;
import com.frazzle.main.domain.socket.game.service.GameService;
import com.frazzle.main.domain.user.entity.User;
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
public class GameController {

    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/start/{boardId}")
    public void startGame(
            @DestinationVariable int boardId,
            StartRequestDto startRequestDto,
            SimpMessageHeaderAccessor accessor) {

        log.info("시작");
        gameService.startGame(startRequestDto);

        simpMessagingTemplate.convertAndSend("/sub/start/" + boardId);
    }

    //움직임 보여주는 메서드
    @MessageMapping("/move/puzzle/{boardId}")
    public void movePuzzle(
            @DestinationVariable int boardId,
            MoveRequestDto moveRequestDto,
            SimpMessageHeaderAccessor accessor
    ) {
        gameService.movePuzzle(boardId, moveRequestDto);
    }

    //사용자가 놓아줄 때 메서드
    @MessageMapping("/check/puzzle/{boardId}")
    public void checkPuzzle(
            @DestinationVariable int boardId,
            ReleaseRequestDto requestDto,
            SimpMessageHeaderAccessor accessor
    ) {
        //이메일로부터 사용자 찾기
        String email = (String) accessor.getSessionAttributes().get("senderEmail");
        gameService.checkPuzzle(boardId, email, requestDto);
    }

}
