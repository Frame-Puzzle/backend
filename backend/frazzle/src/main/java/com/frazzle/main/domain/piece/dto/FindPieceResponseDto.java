package com.frazzle.main.domain.piece.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPieceResponseDto {

    private String imgUrl;

    private String comment;

    @Builder
    public FindPieceResponseDto(String imgUrl, String comment) {
        this.imgUrl = imgUrl;
        this.comment = comment;
    }

    public static FindPieceResponseDto createPieceDto(String imgUrl, String comment) {
        return FindPieceResponseDto.builder().imgUrl(imgUrl).comment(comment).build();
    }
}
