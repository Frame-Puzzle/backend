package com.frazzle.main.domain.board.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.directory.entity.Directory;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

    List<Board> findBoards(int directoryId);
    void deleteBoardByDirectory(Directory directory);
    Optional<String> findThumbnailUrlByBoardId(int boardId);
}
