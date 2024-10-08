package com.frazzle.main.domain.usernotification.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.notification.entity.QNotification;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.usernotification.entity.QUserNotification;
import com.frazzle.main.domain.usernotification.entity.UserNotification;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserNotificationRepositoryImpl implements UserNotificationRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QUserNotification userNotification = QUserNotification.userNotification;
    private final QNotification notification = QNotification.notification;

    @Override
    public void deleteByDirectory(Directory directory) {
        JPQLQuery<Notification> subQuery = JPAExpressions
                .selectFrom(notification)
                .where(notification.directory.eq(directory));

        queryFactory.delete(userNotification)
                .where(userNotification.notification.in(subQuery))
                .execute();
    }

    @Override
    public void  deleteByDirectoryAndUser(Directory directory, User user) {
        JPQLQuery<Notification> subQuery = JPAExpressions
                .selectFrom(notification)
                .where(notification.directory.eq(directory));

        queryFactory.delete(userNotification)
                .where(userNotification.notification.in(subQuery)
                        .and(userNotification.user.eq(user)))
                .execute();
    }

    @Override
    public void deleteByBoard(Board board) {
        JPQLQuery<Notification> subQuery = JPAExpressions
                .selectFrom(notification)
                .where(notification.board.eq(board));

        queryFactory.delete(userNotification)
                .where(userNotification.notification.in(subQuery))
                .execute();
    }

    @Override
    public void updateCancelUserNotification(int notificationId) {
        queryFactory.update(userNotification)
                .set(userNotification.acceptStatus, 3)
                .where(userNotification.notification.notificationId.eq(notificationId))
                .execute();
    }

    @Override
    public void updateMemberCancel(User user, Directory directory) {
        queryFactory.update(userNotification)
                .set(userNotification.acceptStatus, 3)
                .where(userNotification.user.eq(user)
                        .and(userNotification.acceptStatus.eq(0))
                        .and(userNotification.notification.in(
                                JPAExpressions.select(notification)
                                        .from(notification)
                                        .where(notification.directory.eq(directory)
                                                .and(notification.type.eq(0)))
                        )))
                .execute();
    }

    @Override
    public List<UserNotification> findByUserNotNull(User user) {
        JPQLQuery<Notification> subQuery = JPAExpressions
                .selectFrom(notification)
                .where(notification.user.isNotNull());

        return queryFactory.select(userNotification)
                .from(userNotification)
                .where(userNotification.notification.in(subQuery)
                        .and(userNotification.user.eq(user))
                ).fetch();
    }

}
