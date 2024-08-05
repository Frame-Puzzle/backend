package com.frazzle.main.domain.usernotification.repository;

import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.usernotification.entity.QUserNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserNotificationRepositoryImpl implements UserNotificationRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QUserNotification userNotification = QUserNotification.userNotification;

    @Override
    public long  deleteByNotification(List<Notification> notification) {
        return queryFactory.delete(userNotification)
                .where(userNotification.notification.in(notification))
                .execute();
    }
}
