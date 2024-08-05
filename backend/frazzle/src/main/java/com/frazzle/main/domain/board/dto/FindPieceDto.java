package com.frazzle.main.domain.board.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPieceDto {

    private String imgUrl;
    private String comment;

    @Builder
    public FindPieceDto(String imgUrl, String comment) {
        this.imgUrl = imgUrl;
        this.comment = comment;
    }

    public static FindPieceDto createPieceDto(String imgUrl, String comment) {
        return FindPieceDto.builder()
                .imgUrl(imgUrl)
                .comment(comment)
                .build();
    }
}
