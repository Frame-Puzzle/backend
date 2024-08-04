package com.frazzle.main.domain.notification.repository;


import com.frazzle.main.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>, NotificationRepositoryCustom {
    Notification findByNotificationId(int notificationId);
    List<Notification> findByDirectory_DirectoryId(int directoryId);

    @Override
    long deleteByNotification(List<Notification> notifications);
}
