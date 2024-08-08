package com.frazzle.main.domain.directory.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.entity.QBoard;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.entity.QDirectory;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.userdirectory.entity.QUserDirectory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DirectoryRepositoryImpl implements DirectoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    private QDirectory directory= QDirectory.directory;
    private QUserDirectory userDirectory= QUserDirectory.userDirectory;
    private QBoard board= QBoard.board;

    @Override
    public List<Directory> findMyDirectory(User user, String category) {
        JPQLQuery<Directory> subquery = JPAExpressions
                .select(userDirectory.directory)
                .from(userDirectory)
                .where(userDirectory.user.eq(user).and(userDirectory.isAccept.eq(true)));

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(directory.in(subquery));

        //카테고리가 null이 아니면 where절에 추가
        if(category != null) {
            builder.and(directory.category.eq(category));
        }

        return queryFactory
                .selectFrom(directory)
                .where(builder)
                .orderBy(directory.modifiedAt.desc())
                .fetch();
    }

    @Override
    public Optional<Directory> findByBoardId(int boardId) {
        JPQLQuery<Directory> subquery = JPAExpressions
                .select(board.directory)
                .from(board)
                .where(board.boardId.eq(boardId));

        return Optional.ofNullable(queryFactory
                .selectFrom(directory)
                .where(directory.eq(subquery))
                .fetchOne()); // 단일 결과를 반환하도록 수정
    }

}
