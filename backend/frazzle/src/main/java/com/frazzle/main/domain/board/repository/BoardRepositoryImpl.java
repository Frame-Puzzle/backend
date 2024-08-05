package com.frazzle.main.domain.board.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.entity.QBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QBoard board = QBoard.board;

    @Override
    public List<Board> findBoards(int directoryId) {
        return queryFactory.selectFrom(board)
                .where(board.directory.directoryId.eq(directoryId))
                .orderBy(board.boardInNumber.desc())
                .fetch();
    }

    @Override
    public long deleteBoardByBoards(List<Board> boards) {
        return queryFactory.delete(board)
                .where(board.in(boards))
                .execute();
    }

    @Override
    public Optional<String> findThumbnailUrlByBoardId(int boardId) {
        String thumbnailUrl = queryFactory.select(board.thumbnailUrl)
                .from(board)
                .where(board.boardId.eq(boardId))
                .fetchOne();

        return Optional.ofNullable(thumbnailUrl);
    }
}
