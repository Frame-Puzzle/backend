package com.frazzle.main.domain.user.repository;

import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.user.entity.QUser;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.userdirectory.entity.QUserDirectory;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private QUser user = QUser.user;
    private QUserDirectory userDirectory = QUserDirectory.userDirectory;

    @Override
    public List<User> findUsersByEmail(String email, Directory directory) {
        JPQLQuery<User> subquery = JPAExpressions
                .select(userDirectory.user)
                .from(userDirectory)
                .where(userDirectory.directory.eq(directory));

        return queryFactory
                .select(user)
                .from(user)
                .where(user.email.like(email + "%")
                        .and(user.notIn(subquery)))
                .fetch();
    }
}
