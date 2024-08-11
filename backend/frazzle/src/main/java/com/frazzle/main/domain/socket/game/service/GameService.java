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

import java.util.*;
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
            log.info("");
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

        MoveResponseDto responseDto = MoveResponseDto.createResponseDto(idx, x, y);

        simpMessagingTemplate.convertAndSend("/sub/game/" + boardId+"/puzzle/move", responseDto);
    }

    public void checkPuzzle(int boardId, String email, ReleaseRequestDto requestDto) {
        //유저 찾기
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        int currentIdx = requestDto.getCurrentIdx();
        int[] nextIdx = requestDto.getNextIdxList();
        int puzzleSize = requestDto.getPuzzleSize();
        Game game = gameMap.get(boardId);

        GamePuzzle[] gamePuzzleList = game.getGamePuzzle();
        Map<Integer, GamePlayer> gamePlayerMap = game.getGamePlayerMap();
        GamePlayer gamePlayer = gamePlayerMap.get(user.getUserId());

        int size = game.getSize();

        for(int i=0; i<4;i++) {
            Boolean flag = false;
            //비어있는게 아닐 경우
            if(nextIdx[i]!= 0) {
                switch (i) {
                    //상
                    case 0:
                        if(nextIdx[i]+size==currentIdx) {
                            flag = true;
                        }
                        break;
                    //하
                    case 1:
                        if(nextIdx[i]-size==currentIdx) {
                            flag = true;
                        }
                        break;
                    //좌
                    case 2:
                        if(nextIdx[i]+1==currentIdx) {
                            flag = true;
                        }
                        break;
                    //우
                    case 3:
                        if(nextIdx[i]-1==currentIdx) {
                            flag = true;
                        }
                        break;
                }
            }
            if(flag) {
                //그룹화
                union(currentIdx, nextIdx[i], gamePuzzleList);
                //현재 플레이어 맞춘 횟수 +1
                gamePlayer.upCount();
            }
        }

        float x = requestDto.getX();
        float y = requestDto.getY();

        moveSameGroup(x, y, currentIdx, gamePuzzleList[currentIdx].getGroup(), game, puzzleSize);

        ReleaseResponseDto responseDto = ReleaseResponseDto.createResponseDto(gamePuzzleList);

        simpMessagingTemplate.convertAndSend("/sub/game/"+boardId+"/puzzle/check/", responseDto);
    }

    private class PuzzlePosition {
        int r;
        int c;

        PuzzlePosition(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    //위치 옮기기
    private void moveSameGroup(float x, float y, int currentIdx, int group, Game game, int puzzleSize) {
        Queue<PuzzlePosition> queue = new ArrayDeque<>();

        int size = game.getSize();
        GamePuzzle[] gamePuzzleList = game.getGamePuzzle();

        int curR = currentIdx / size;
        int curC = currentIdx % size;

        queue.offer(new PuzzlePosition(curR, curC));

        Boolean[][] visited = new Boolean[size][size];
        //현재 위치 x,y좌표 추가
        gamePuzzleList[currentIdx].updatePosition(x, y);


        while (!queue.isEmpty()) {
            PuzzlePosition curIdx = queue.poll();

            for(int d=0; d<4; d++) {
                int nr = curIdx.r + dr[d];
                int nc = curIdx.c + dc[d];

                if(nr < 0 || nr >= size || nc < 0 || nc >= size) continue;

                int nextIdx = nr*size + nc;

                //같은 그룹만 되게
                if(gamePuzzleList[nextIdx].getGroup()!= group) continue;

                //상 하 좌 우
                float nextX = 0;
                float nextY = 0;
                float curX = gamePuzzleList[curIdx.r*size+curIdx.c].getX();
                float curY = gamePuzzleList[curIdx.r*size+curIdx.c].getY();

                switch (d) {
                    case 0:
                        nextX = curX - puzzleSize;
                        nextY = curY;
                        break;
                    case 1:
                        nextX = curX + puzzleSize;
                        nextY = curY;
                        break;
                    case 2:
                        nextX = curX;
                        nextY = curY - puzzleSize;
                        break;
                    case 3:
                        nextX = curX;
                        nextY = curY + puzzleSize;
                        break;
                }

                gamePuzzleList[nextIdx].updatePosition(nextX, nextY);
            }
        }
    }

    private int[] dr = {-1, 1, 0, 0};
    private int[] dc = {0, 0, -1, 1};

    //그룹 찾기
    private int findParent(int x, GamePuzzle[] parent) {
        if(parent[x].getGroup() != x) parent[x].updateGroup(findParent(parent[x].getGroup(), parent));
        return parent[x].getGroup();
    }

    //그룹 합치기
    private void union(int a, int b, GamePuzzle[] parent) {
        int parentA = findParent(a, parent);
        int parentB = findParent(b, parent);

        if(parentA > parentB) {
            parent[parentA].updateGroup(parentB);
        }

        else if(parentB > parentA) {
            parent[parentB].updateGroup(parentA);
        }
    }
}