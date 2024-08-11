package com.frazzle.main.domain.socket.game.dto;

import com.frazzle.main.domain.socket.game.entity.GamePuzzle;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CheckResponseDto {
    private GamePuzzle[] gamePuzzleList;

    @Builder
    private CheckResponseDto(GamePuzzle[] gamePuzzleList) {
        this.gamePuzzleList = gamePuzzleList;
    }

    public static CheckResponseDto createResponseDto(GamePuzzle[] gamePuzzleList) {
        return CheckResponseDto.builder()
                .gamePuzzleList(gamePuzzleList)
                .build();
    }
}
