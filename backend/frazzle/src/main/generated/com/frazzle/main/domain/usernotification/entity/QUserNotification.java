package com.frazzle.main.domain.usernotification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserNotification is a Querydsl query type for UserNotification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserNotification extends EntityPathBase<UserNotification> {

    private static final long serialVersionUID = 1558371595L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserNotification userNotification = new QUserNotification("userNotification");

    public final StringPath acceptStatus = createString("acceptStatus");

    public final BooleanPath isRead = createBoolean("isRead");

    public final com.frazzle.main.domain.notification.entity.QNotification notification;

    public final com.frazzle.main.domain.user.entity.QUser user;

    public final NumberPath<Integer> userNotificationId = createNumber("userNotificationId", Integer.class);

    public QUserNotification(String variable) {
        this(UserNotification.class, forVariable(variable), INITS);
    }

    public QUserNotification(Path<? extends UserNotification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserNotification(PathMetadata metadata, PathInits inits) {
        this(UserNotification.class, metadata, inits);
    }

    public QUserNotification(Class<? extends UserNotification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.notification = inits.isInitialized("notification") ? new com.frazzle.main.domain.notification.entity.QNotification(forProperty("notification"), inits.get("notification")) : null;
        this.user = inits.isInitialized("user") ? new com.frazzle.main.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

