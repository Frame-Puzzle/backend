package com.frazzle.main.domain.board.repository;

import com.frazzle.main.domain.board.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

    List<Board> findBoards(int directoryId);
    long deleteBoardByBoards(List<Board> boards);
    Optional<String> findThumbnailUrlByBoardId(int boardId);
}
