package com.frazzle.main.domain.board.dto;

import com.frazzle.main.domain.piece.entity.Piece;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FindBoardAndPiecesResponseDto {

    private String[] keyword;
    private String category;
    private int boardSize;
    //private boolean isComplete;

    private List<Piece> pieceList;
    private int pieceId;
    private int authority;
    private String thumbnailer;
    private String directoryName;
    private int boardNum;

}
