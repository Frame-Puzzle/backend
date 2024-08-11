package com.frazzle.main.domain.board.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FindGameRoomResponseDto {
    private Boolean exist;
    private int size;


    @Builder
    private FindGameRoomResponseDto(Boolean exist, int size) {
        this.exist = exist;
        this.size = size;
    }


    public static FindGameRoomResponseDto createResponseDto(Boolean exist, int size) {
        return FindGameRoomResponseDto.builder()
                .exist(exist)
                .size(size)
                .build();
    }
}
