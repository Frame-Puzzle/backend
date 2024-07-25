package com.frazzle.main.domain.directory.repository;

import com.frazzle.main.domain.directory.entity.QDirectory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DirectoryRepositoryImpl implements DirectoryRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    private QDirectory directory= QDirectory.directory;

    @Override
    public long updateNameByDirectoryId(int directoryId, String directoryName) {
        return jpaQueryFactory
                .update(directory)
                .set(directory.directoryName, directoryName)
                .where(directory.directoryId.eq(directoryId))
                .execute();
    }
}
