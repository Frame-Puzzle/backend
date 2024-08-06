package com.frazzle.main.domain.usernotification.repository;

import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.notification.entity.QNotification;
import com.frazzle.main.domain.usernotification.entity.QUserNotification;
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
    public void  deleteByDirectory(Directory directory) {
        JPQLQuery<Notification> subQuery = JPAExpressions
                .selectFrom(notification)
                .where(notification.directory.eq(directory));

        queryFactory.delete(userNotification)
                .where(userNotification.notification.in(subQuery))
                .execute();
    }
}
