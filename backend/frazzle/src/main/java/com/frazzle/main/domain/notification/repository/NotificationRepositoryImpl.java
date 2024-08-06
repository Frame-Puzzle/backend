package com.frazzle.main.domain.notification.repository;

import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.notification.entity.QNotification;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.usernotification.entity.QUserNotification;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private QNotification notification = QNotification.notification;

    private QUserNotification userNotification = QUserNotification.userNotification;


    @Override
    public List<Notification> findByUser(User user) {
        JPQLQuery<Notification> subquery = JPAExpressions
                .select(userNotification.notification)
                .from(userNotification)
                .where(userNotification.user.eq(user));

        List<Notification> notifications = jpaQueryFactory
                .select(notification)
                .from(notification)
                .where(notification.in(subquery))
                .orderBy(notification.createdAt.desc())
                .fetch();


        return notifications;
    }
}
