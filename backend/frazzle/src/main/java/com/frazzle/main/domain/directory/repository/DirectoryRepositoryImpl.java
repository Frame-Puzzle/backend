package com.frazzle.main.domain.directory.repository;

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

@RequiredArgsConstructor
public class DirectoryRepositoryImpl implements DirectoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    private QDirectory directory= QDirectory.directory;
    private QUserDirectory userDirectory= QUserDirectory.userDirectory;

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

}
