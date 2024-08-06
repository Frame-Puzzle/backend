package com.frazzle.main.domain.usernotification.repository;

import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.notification.entity.Notification;

import java.util.List;

public interface UserNotificationRepositoryCustom {

    void deleteByDirectory(Directory directory);

    void deleteByNotification(List<Notification> notification);
}
