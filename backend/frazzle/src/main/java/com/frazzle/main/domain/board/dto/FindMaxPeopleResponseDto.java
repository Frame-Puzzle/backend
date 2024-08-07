package com.frazzle.main.domain.board.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindMaxPeopleResponseDto {
    private String imgUrl;
    private String directoryName;
    private int boardNum;

    @Builder
    private FindMaxPeopleResponseDto(String imgUrl, String directoryName, int boardNum) {
        this.imgUrl = imgUrl;
        this.directoryName = directoryName;
        this.boardNum = boardNum;
    }

    public static FindMaxPeopleResponseDto createResponseDto(String imgUrl, String directoryName, int boardNum) {
        return FindMaxPeopleResponseDto.builder()
                .imgUrl(imgUrl)
                .directoryName(directoryName)
                .boardNum(boardNum)
                .build();
    }
}
