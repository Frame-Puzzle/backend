package com.frazzle.main.domain.board.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindInGameResponseDto {
    private boolean exist;

    @Builder
    private FindInGameResponseDto(boolean exist) {
        this.exist = exist;
    }

    public static FindInGameResponseDto createResponseDto(boolean exist) {
        return FindInGameResponseDto.builder()
                .exist(exist)
                .build();
    }
}
