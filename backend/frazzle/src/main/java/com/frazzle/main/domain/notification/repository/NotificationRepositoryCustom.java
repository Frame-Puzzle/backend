package com.frazzle.main.domain.notification.repository;

import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.user.entity.User;

import java.util.List;

public interface NotificationRepositoryCustom {
    List<Notification> findByUser(User user);
    long deleteByNotification(List<Notification> notifications);
}
