package com.frazzle.main.domain.socket.game.service;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.entity.BoardClearTypeFlag;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.piece.entity.Piece;
import com.frazzle.main.domain.piece.repository.PieceRepository;
import com.frazzle.main.domain.socket.game.dto.*;
import com.frazzle.main.domain.socket.game.entity.Game;
import com.frazzle.main.domain.socket.game.entity.GamePlayer;
import com.frazzle.main.domain.socket.game.entity.GamePuzzle;
import com.frazzle.main.domain.socket.roby.controller.RobyController;
import com.frazzle.main.domain.socket.roby.entity.Roby;
import com.frazzle.main.domain.socket.roby.entity.RobyUser;
import com.frazzle.main.domain.socket.roby.service.RobyService;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final Map<Integer, Game> gameMap = new HashMap<>();
    private final PieceRepository pieceRepository;
    private final Map<Integer, ScheduledFuture<?>> timers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RobyService robyService;
    private final RobyController robyController;


    public void startGame(StartRequestDto requestDto) {
        int boardId = requestDto.getBoardId();
        int size = requestDto.getSize();

        if (!gameMap.containsKey(boardId)) {

            List<RobyUser> userList = robyService.getUsersInRoby(boardId);

            //유저들 추가
            Map<Integer, GamePlayer> gamePlayerMap = new HashMap<>();
            for (RobyUser user : userList) {
                GamePlayer gamePlayer = GamePlayer.createGamePlayer(user);

                gamePlayerMap.put(user.getUserId(), gamePlayer);
            }

            //퍼즐들 추가
            //초기 그룹은 자신의 인덱스로 하기
            GamePuzzle[] gamePuzzleList = new GamePuzzle[size*size];
            for (int i = 0; i < size*size; i++) {
                gamePuzzleList[i] = GamePuzzle.createGamePuzzle(i);
            }

            Board board = boardRepository.findByBoardId(boardId).orElseThrow(
                    () -> new CustomException(ErrorCode.NOT_EXIST_BOARD)
            );

            //보드와 연관된 퍼즐조각중 사람 수가 가장 많은 거 하나만 가져오기
            Piece piece = pieceRepository.findByBoardOrderByPeopleCountDesc(board).get(0);

            //게임 생성 후
            Game game = Game.createGame(
                    piece.getImageUrl(),
                    size,
                    gamePuzzleList,
                    gamePlayerMap
            );

            gameMap.put(boardId, game);

            List<Integer> numList = new ArrayList<>();

            for(int i=0; i<size*size;i++) {
                numList.add(i);
            }

            Collections.shuffle(numList);

            int[] numArray = numList.stream().mapToInt(Integer::intValue).toArray();

            game.updateNumArray(numArray);

        }

        Game game = gameMap.get(boardId);


        StartResponseDto responseDto = StartResponseDto.createResponseDto();

        simpMessagingTemplate.convertAndSend("/sub/start/" + boardId, responseDto);

        log.info(game.toString());

        GameResponseDto gameResponseDto = GameResponseDto.createResponseDto(game);

        simpMessagingTemplate.convertAndSend("/sub/game/info/"+boardId, gameResponseDto);

        //스톱워치 시작
        timer(boardId);
    }

    public void endPuzzle(int boardId, String email, int time) {
        //유저 찾기
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        Board board = boardRepository.findByBoardId(boardId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_BOARD)
        );


        timers.get(boardId).cancel(true);

        EndResponseDto responseDto = EndResponseDto.createEndResponseDto(time, user.getNickname());

        simpMessagingTemplate.convertAndSend("/sub/game/"+ boardId+"/puzzle/end", responseDto);

        //썸네일러 업데이트
        board.updateUser(user);
        board.changeClearType(BoardClearTypeFlag.PUZZLE_GAME_CLEARED);
        boardRepository.save(board);

        //삭제
        timers.remove(boardId);
        gameMap.remove(boardId);
    }

    public void exitPuzzle(int boardId, String email) {
        //유저 찾기
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        Game game = gameMap.get(boardId);
        Map<Integer, GamePlayer> gamePlayerMap = game.getGamePlayerMap();
        gamePlayerMap.remove(user.getUserId());

        //모두 다 나가면
        if(gamePlayerMap.isEmpty()) {
            log.info("모두 나감");

            robyService.removeRoby(boardId);
            timers.get(boardId).cancel(true);
            timers.remove(boardId);
            gameMap.remove(boardId);
        }

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

    public boolean findGame(int boardId) {
        if(gameMap.containsKey(boardId)) {
            return true;
        }
        return false;
    }
}