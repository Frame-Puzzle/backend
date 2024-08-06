package com.frazzle.main.domain.directory.dto;

import com.frazzle.main.domain.board.entity.Board;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardListDto {

    private int boardId;
    private int boardSize;
    private int boardName;
    private String thumbnailUrl;

    @Builder
    private BoardListDto(int boardId, int boardSize, int boardName, String thumbnailUrl) {
        this.boardId = boardId;
        this.boardSize = boardSize;
        this.boardName = boardName;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static BoardListDto createBoardList(Board board) {
        return BoardListDto.builder()
                .boardId(board.getBoardId())
                .boardSize(board.getBoardSize())
                .boardName(board.getBoardInNumber())
                .thumbnailUrl(board.getThumbnailUrl())
                .build();
    }
}
