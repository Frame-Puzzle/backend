package com.frazzle.main.domain.socket.game.controller;

import com.frazzle.main.domain.socket.game.dto.*;
import com.frazzle.main.domain.socket.game.service.GameService;
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

        StartResponseDto responseDto = StartResponseDto.createResponseDto();

        simpMessagingTemplate.convertAndSend("/sub/start/" + boardId, responseDto);

        gameService.startGame(startRequestDto);

    }

    // 움직임 보여주는 메서드
    @MessageMapping("/move/puzzle/{boardId}")
    public void movePuzzle(
            @DestinationVariable int boardId,
            MoveRequestDto moveRequestDto,
            SimpMessageHeaderAccessor accessor
    ) {
        String email = (String) accessor.getSessionAttributes().get("senderEmail");

        gameService.movePuzzle(boardId, moveRequestDto.getIdx(), email);
    }

    // 놓을 때 보여주는 메서드
    @MessageMapping("/release/puzzle/{boardId}")
    public void releasePuzzle(
            @DestinationVariable int boardId,
            ReleaseRequestDto releaseRequestDto,
            SimpMessageHeaderAccessor accessor
    ) {
        log.info("movePuzzle called with boardId={}, index={}, x={}, y={}",
                boardId, releaseRequestDto.getIndex(), releaseRequestDto.getX(), releaseRequestDto.getY());

        gameService.releasePuzzle(boardId, releaseRequestDto);
        log.info("movePuzzle processing completed for boardId={}", boardId);
    }

    // 퍼즐 연결했을 때 메서드
    @MessageMapping("/check/puzzle/{boardId}")
    public void checkPuzzle(
            @DestinationVariable int boardId,
            CheckRequestDto requestDto,
            SimpMessageHeaderAccessor accessor
    ) {
        // 이메일로부터 사용자 찾기
        String email = (String) accessor.getSessionAttributes().get("senderEmail");
        log.info("checkPuzzle called with boardId={}, email={}, currentIdx={}",
                boardId, email, requestDto.getCurrentIdx());

        gameService.checkPuzzle(boardId, email, requestDto);
        log.info("checkPuzzle processing completed for boardId={}", boardId);
    }

    // 퍼즐 연결했을 때 메서드
    @MessageMapping("/end/puzzle/{boardId}")
    public void endPuzzle(
            @DestinationVariable int boardId,
            EndRequestDto requestDto,
            SimpMessageHeaderAccessor accessor
    ) {
        // 이메일로부터 사용자 찾기
        String email = (String) accessor.getSessionAttributes().get("senderEmail");

        gameService.endPuzzle(boardId, email, requestDto);
        log.info("checkPuzzle processing completed for boardId={}", boardId);
    }

}
