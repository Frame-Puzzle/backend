package com.frazzle.main.domain.notification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotification is a Querydsl query type for Notification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotification extends EntityPathBase<Notification> {

    private static final long serialVersionUID = 257440085L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotification notification = new QNotification("notification");

    public final com.frazzle.main.domain.board.entity.QBoard board;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final com.frazzle.main.domain.directory.entity.QDirectory directory;

    public final StringPath keyword = createString("keyword");

    public final NumberPath<Integer> notificationId = createNumber("notificationId", Integer.class);

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final com.frazzle.main.domain.user.entity.QUser user;

    public QNotification(String variable) {
        this(Notification.class, forVariable(variable), INITS);
    }

    public QNotification(Path<? extends Notification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotification(PathMetadata metadata, PathInits inits) {
        this(Notification.class, metadata, inits);
    }

    public QNotification(Class<? extends Notification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.frazzle.main.domain.board.entity.QBoard(forProperty("board"), inits.get("board")) : null;
        this.directory = inits.isInitialized("directory") ? new com.frazzle.main.domain.directory.entity.QDirectory(forProperty("directory")) : null;
        this.user = inits.isInitialized("user") ? new com.frazzle.main.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

