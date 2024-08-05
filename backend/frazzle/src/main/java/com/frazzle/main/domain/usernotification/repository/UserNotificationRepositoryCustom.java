package com.frazzle.main.domain.usernotification.repository;

import com.frazzle.main.domain.notification.entity.Notification;

import java.util.List;

public interface UserNotificationRepositoryCustom {

    long deleteByNotification(List<Notification> notification);
}
