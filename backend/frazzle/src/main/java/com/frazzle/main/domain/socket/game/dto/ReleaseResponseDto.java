package com.frazzle.main.domain.socket.game.dto;

import com.frazzle.main.domain.socket.game.entity.GamePuzzle;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ReleaseResponseDto {
    private GamePuzzle[] gamePuzzleList;

    @Builder
    private ReleaseResponseDto(GamePuzzle[] gamePuzzleList) {
        this.gamePuzzleList = gamePuzzleList;
    }

    public static ReleaseResponseDto createResponseDto(GamePuzzle[] gamePuzzleList) {
        return ReleaseResponseDto.builder()
                .gamePuzzleList(gamePuzzleList)
                .build();
    }
}
