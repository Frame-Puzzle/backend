package com.frazzle.main.domain.piece.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PieceDto {

    private String imgUrl;

    private String comment;
}
