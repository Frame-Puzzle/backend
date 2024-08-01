package com.frazzle.main.domain.piece.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PieceDto {

    private String imgUrl;

    private String comment;

    @Builder
    public PieceDto(String imgUrl, String comment) {
        this.imgUrl = imgUrl;
        this.comment = comment;
    }

    public static PieceDto createPieceDto(String imgUrl, String comment) {
        return new PieceDto(imgUrl, comment);
    }
}
