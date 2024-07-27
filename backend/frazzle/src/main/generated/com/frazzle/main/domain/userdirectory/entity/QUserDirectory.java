package com.frazzle.main.domain.userdirectory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserDirectory is a Querydsl query type for UserDirectory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserDirectory extends EntityPathBase<UserDirectory> {

    private static final long serialVersionUID = 1749907589L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserDirectory userDirectory = new QUserDirectory("userDirectory");

    public final com.frazzle.main.domain.directory.entity.QDirectory directory;

    public final BooleanPath isAccept = createBoolean("isAccept");

    public final com.frazzle.main.domain.user.entity.QUser user;

    public final NumberPath<Integer> userDirectoryId = createNumber("userDirectoryId", Integer.class);

    public QUserDirectory(String variable) {
        this(UserDirectory.class, forVariable(variable), INITS);
    }

    public QUserDirectory(Path<? extends UserDirectory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserDirectory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserDirectory(PathMetadata metadata, PathInits inits) {
        this(UserDirectory.class, metadata, inits);
    }

    public QUserDirectory(Class<? extends UserDirectory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.directory = inits.isInitialized("directory") ? new com.frazzle.main.domain.directory.entity.QDirectory(forProperty("directory")) : null;
        this.user = inits.isInitialized("user") ? new com.frazzle.main.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

