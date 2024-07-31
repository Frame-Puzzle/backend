package com.frazzle.main.domain.usernotification.repository;

import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.usernotification.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Integer> {
    List<UserNotification> findByUser(User user);
}
