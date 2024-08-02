package com.frazzle.main.domain.board.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FindBoardAndPiecesResponseDto {

    private String[] keyword;
    private String category;
    private String directoryName;
    private String boardNum;
    private int boardSize;
    private String thumbnailer;

    private PieceListResponseDto[] pieceList;

    @Builder
    private FindBoardAndPiecesResponseDto(
            String[] keyword,
            String category,
            String directoryName,
            String boardNum,
            int boardSize,
            String thumbnailer,
            PieceListResponseDto[] pieceList){
        this.keyword = keyword;
        this.category = category;
        this.directoryName = directoryName;
        this.boardNum = boardNum;
        this.boardSize = boardSize;
        this.thumbnailer = thumbnailer;
        this.pieceList = pieceList;
    }

    public static FindBoardAndPiecesResponseDto createFindBoardAndPiecesResponseDto(
            String[] keyword,
            String category,
            String directoryName,
            String boardNum,
            int boardSize,
            String thumbnailer,
            PieceListResponseDto[] pieceList) {
        return FindBoardAndPiecesResponseDto.builder()
                .keyword(keyword)
                .category(category)
                .directoryName(directoryName)
                .boardNum(boardNum)
                .boardSize(boardSize)
                .thumbnailer(thumbnailer)
                .pieceList(pieceList)
                .build();
    }
}
