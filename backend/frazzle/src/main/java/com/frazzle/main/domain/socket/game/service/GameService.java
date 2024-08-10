package com.frazzle.main.domain.socket.game.service;

import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.socket.game.dto.*;
import com.frazzle.main.domain.socket.game.entity.Game;
import com.frazzle.main.domain.socket.game.entity.GamePlayer;
import com.frazzle.main.domain.socket.game.entity.GamePuzzle;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final DirectoryRepository directoryRepository;
    private final Map<Integer, Game> gameMap;
    private final Map<Integer, ScheduledFuture<?>> timers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final SimpMessagingTemplate simpMessagingTemplate;


    public void startGame(StartRequestDto requestDto) {
        int boardId = requestDto.getBoardId();
        int size = requestDto.getSize();

        if (!gameMap.containsKey(boardId)) {
            List<User> userList = userRepository.findAllUserByBoardId(boardId);
            //유저들 추가
            Map<Integer, GamePlayer> gamePlayerMap = new HashMap<>();
            for (User user : userList) {
                int userId = user.getUserId();

                GamePlayer gamePlayer = GamePlayer.createGamePlayer(userId);


                gamePlayerMap.put(user.getUserId(), gamePlayer);
            }

            //퍼즐들 추가
            //초기 그룹은 자신의 인덱스로 하기
            GamePuzzle[] gamePuzzleList = new GamePuzzle[size];
            for (int i = 0; i < size; i++) {
                gamePuzzleList[i] = GamePuzzle.createGamePuzzle(i);
            }

            //게임 생성 후
            Game game = Game.createGame(
                    size,
                    gamePuzzleList,
                    gamePlayerMap
            );

            gameMap.put(boardId, game);

        }

        //스톱워치 시작
        timer(boardId);
    }

    //스톱워치 시작
    private void timer(int boardId) {
        if (timers.containsKey(boardId)) {
            // 이미 타이머가 실행 중인 경우
            log.info("Timer already running for boardId={}", boardId);
            return;
        }

        final long[] elapsedTime = {0}; // 경과 시간

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            elapsedTime[0]++;
            simpMessagingTemplate.convertAndSend("/sub/game/timer/" + boardId, elapsedTime[0]);
        }, 0, 1, TimeUnit.SECONDS);

        timers.put(boardId, future);
        log.info("Timer started for boardId={}", boardId);
    }


    public void movePuzzle(int boardId, MoveRequestDto moveRequestDto) {

        int idx = moveRequestDto.getIndex();
        float x = moveRequestDto.getX();
        float y = moveRequestDto.getY();
//
//        Game game = gameMap.get(boardId);
//        GamePuzzle gamePuzzle = game.getGamePuzzle()[idx];
//        gamePuzzle.updatePosition(x, y);

        MoveResponseDto responseDto = MoveResponseDto.createResponseDto(idx, x, y);

        simpMessagingTemplate.convertAndSend("/sub/game/" + boardId+"/puzzle/move", responseDto);
    }

    //상하좌우 체크
    //혹은 하 우만 체크
    public void checkPuzzle(int boardId, String email, ReleaseRequestDto requestDto) {
        //유저 찾기
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        ReleaseResponseDto responseDto = new ReleaseResponseDto();

        simpMessagingTemplate.convertAndSend("/sub/game/"+boardId+"/puzzle/check/", responseDto);
    }
}