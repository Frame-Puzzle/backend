package com.frazzle.main.domain.socket.game.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EndResponseDto {
    private Long time;
    private List<EndUserDto> endUserDtoList;

    @Builder
    public EndResponseDto(Long time, List<EndUserDto> endUserDtoList) {
        this.time = time;
        this.endUserDtoList = endUserDtoList;
    }

    public static EndResponseDto createEndResponseDto(Long time, List<EndUserDto> endUserDtoList) {
        return EndResponseDto.builder()
                .time(time)
                .endUserDtoList(endUserDtoList)
                .build();
    }
}
