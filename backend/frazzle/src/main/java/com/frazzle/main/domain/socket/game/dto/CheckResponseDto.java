package com.frazzle.main.domain.socket.game.dto;

import com.frazzle.main.domain.socket.game.entity.GamePuzzle;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CheckResponseDto {
    private int group;
    private GamePuzzle[] gamePuzzleList;

    @Builder
    private CheckResponseDto(int group, GamePuzzle[] gamePuzzleList) {
        this.group = group;
        this.gamePuzzleList = gamePuzzleList;
    }


    public static CheckResponseDto createResponseDto(int group, GamePuzzle[] gamePuzzleList) {
        return CheckResponseDto.builder()
                .group(group)
                .gamePuzzleList(gamePuzzleList)
                .build();
    }
}
