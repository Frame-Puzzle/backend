package com.frazzle.main.domain.piece.repository;


import com.frazzle.main.domain.board.entity.Board;

import java.util.List;

public interface PieceRepositoryCustom {

    long deletePieceByBoards(List<Board> boards);

    void nullifyUserInPiecesByDirectoryAndUser(int userId, int directoryId);
}
