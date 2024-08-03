package com.frazzle.main.domain.board.dto;

import lombok.Builder;

public class CreateBoardResponseDto {

    private int boardId;

    @Builder
    private CreateBoardResponseDto(int boardId) {
        this.boardId = boardId;
    }

    public static CreateBoardResponseDto createBoardResponseDto(int boardId) {
        return CreateBoardResponseDto.builder().boardId(boardId).build();
    }
}
