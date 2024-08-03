package com.frazzle.main.domain.board.repository;

import com.frazzle.main.domain.board.entity.Board;

import java.util.List;

public interface BoardRepositoryCustom {

    List<Board> findBoards(int directoryId);
}
