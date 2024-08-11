package com.frazzle.main.domain.socket.game.entity;

import lombok.*;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game {
    private int size;
    private String imgUrl;
    private GamePuzzle[] gamePuzzle;
    private Map<Integer, GamePlayer> gamePlayerMap;
    private int[] numArray;

    @Builder
    private Game(int size, String imgUrl, GamePuzzle[] gamePuzzle, Map<Integer, GamePlayer> gamePlayerMap, int[] numArray) {
        this.size = size;
        this.imgUrl = imgUrl;
        this.gamePuzzle = gamePuzzle;
        this.gamePlayerMap = gamePlayerMap;
        this.numArray = numArray;
    }

    public static Game createGame(String imgUrl, int size, GamePuzzle[] gamePuzzleList, Map<Integer, GamePlayer> gamePlayerMap) {
        return Game.builder()
                .imgUrl(imgUrl)
                .gamePuzzle(gamePuzzleList)
                .gamePlayerMap(gamePlayerMap)
                .build();
    }

    public void updateNumArray(int[] array) {
        this.numArray = array;
    }
}
