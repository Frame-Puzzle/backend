package com.frazzle.main.domain.socket.game.dto;

import com.frazzle.main.domain.socket.game.entity.GamePuzzle;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ReleaseResponseDto {
    private int group;
    private GamePuzzle[] gamePuzzleList;

    @Builder
    private ReleaseResponseDto(int group, GamePuzzle[] gamePuzzleList) {
        this.group = group;
        this.gamePuzzleList = gamePuzzleList;
    }

    public static ReleaseResponseDto createResponseDto(int group, GamePuzzle[] gamePuzzleList) {
        return ReleaseResponseDto.builder()
                .group(group)
                .gamePuzzleList(gamePuzzleList)
                .build();
    }
}
