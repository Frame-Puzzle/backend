package com.frazzle.main.domain.piece.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.entity.QBoard;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.piece.entity.QPiece;
import com.frazzle.main.domain.user.entity.User;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PieceRepositoryImpl implements PieceRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QPiece piece = QPiece.piece;
    private final QBoard board = QBoard.board;

    @Override
    public long deletePieceByBoards(List<Board> boards) {
        return queryFactory.delete(piece)
                .where(piece.board.in(boards))
                .execute();
    }

    @Override
    public void nullifyUserInPiecesByDirectoryAndUser(int userId, int directoryId) {
        JPQLQuery<Board> subQuery = JPAExpressions
                .selectFrom(board)
                .where(board.directory.directoryId.eq(directoryId));

        queryFactory.update(piece)
                .set(piece.user, (User)null)
                .where(
                    piece.board.in(subQuery).and(piece.user.userId.eq(userId))
                ).execute();
    }
}
