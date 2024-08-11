package com.frazzle.main.domain.socket.game.entity;

import lombok.*;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game {
    private int size;
    private GamePuzzle[] gamePuzzle;
    private Map<Integer, GamePlayer> gamePlayerMap;
    private int[] numArray;

    @Builder
    private Game(int size, GamePuzzle[] gamePuzzle, Map<Integer, GamePlayer> gamePlayerMap) {
        this.size = size;
        this.gamePuzzle = gamePuzzle;
        this.gamePlayerMap = gamePlayerMap;
    }

    public static Game createGame(int size, GamePuzzle[] gamePuzzleList, Map<Integer, GamePlayer> gamePlayerMap) {
        return Game.builder()
                .gamePuzzle(gamePuzzleList)
                .gamePlayerMap(gamePlayerMap)
                .build();
    }

    public void updateNumArray(int[] array) {
        this.numArray = array;
    }
}
