package com.frazzle.main.domain.usernotification.repository;

import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.usernotification.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Integer>, UserNotificationRepositoryCustom {
    List<UserNotification> findByUser(User user);
    UserNotification findByUserAndNotification(User user, Notification notification);

    @Override
    long deleteByNotification(List<Notification> notification);
}
