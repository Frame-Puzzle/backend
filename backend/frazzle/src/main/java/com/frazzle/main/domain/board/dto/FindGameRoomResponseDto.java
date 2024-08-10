package com.frazzle.main.domain.board.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FindGameRoomResponseDto {
    private boolean exist;

    @Builder
    private FindGameRoomResponseDto(Boolean exist) {
        this.exist = exist;
    }

    public static FindGameRoomResponseDto createResponseDto(Boolean exist) {
        return FindGameRoomResponseDto.builder()
                .exist(exist)
                .build();
    }
}
