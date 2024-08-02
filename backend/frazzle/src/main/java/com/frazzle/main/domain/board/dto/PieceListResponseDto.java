package com.frazzle.main.domain.board.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PieceListResponseDto {

    private int pieceId;
    private int authority;
    private int row;
    private int col;

    @Builder
    private PieceListResponseDto(int pieceId, int authority, int row, int col) {
        this.pieceId = pieceId;
        this.authority = authority;
        this.row = row;
        this.col = col;
    }

    public static PieceListResponseDto createPieceListResponseDto(int pieceId, int authority, int row, int col){
        return PieceListResponseDto.builder()
                .pieceId(pieceId)
                .authority(authority)
                .row(row)
                .col(col)
                .build();
    }
}
