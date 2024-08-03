package com.frazzle.main.domain.board.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.entity.QBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QBoard board;

    @Override
    public List<Board> findBoards(int directoryId) {
        return queryFactory.selectFrom(board)
                .where(board.directory.directoryId.eq(directoryId))
                .orderBy(board.boardInNumber.desc())
                .fetch();
    }
}
