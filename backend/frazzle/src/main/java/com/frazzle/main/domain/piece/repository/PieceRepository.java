package com.frazzle.main.domain.piece.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.piece.entity.Piece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PieceRepository extends JpaRepository<Piece, Integer>, PieceRepositoryCustom {

    Optional<Piece> findPieceByPieceId(int pieceId);

    List<Piece> findAllByBoardBoardId(int boardId);

    @Override
    long deletePieceByBoards(List<Board> boards);

    @Override
    void nullifyUserInPiecesByDirectoryAndUser(int userId, int directoryId);
}
