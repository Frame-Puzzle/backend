package com.frazzle.main.domain.userdirectory.repository;

import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.userdirectory.entity.QUserDirectory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserDirectoryRepositoryImpl implements UserDirectoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QUserDirectory userDirectory = QUserDirectory.userDirectory;

    @Override
    public List<Integer> findDirectoryIdByUserAndIsAccept(User user, boolean isAccept) {
        return queryFactory.select(userDirectory.directory.directoryId)
                .from(userDirectory)
                .where(userDirectory.user.eq(user).and(userDirectory.isAccept.eq(isAccept)))
                .fetch();
    }
}
