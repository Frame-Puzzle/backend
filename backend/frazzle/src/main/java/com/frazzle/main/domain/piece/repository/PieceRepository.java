package com.frazzle.main.domain.piece.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.piece.entity.Piece;
import com.frazzle.main.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PieceRepository extends JpaRepository<Piece, Integer>, PieceRepositoryCustom {

    Optional<Piece> findPieceByPieceId(int pieceId);

    List<Piece> findAllByBoardBoardId(int boardId);

    @Override
    void deletePieceByDirectory(int directoryId);

    @Override
    void nullifyUserInPiecesByDirectoryAndUser(int userId, int directoryId);
    void deleteAllByBoard(Board board);
    List<Piece> findAllByUser(User user);

    Piece findByBoardOrderByPeopleCountDesc(Board board);
}
