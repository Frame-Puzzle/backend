package com.frazzle.main.domain.notification.repository;


import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>, NotificationRepositoryCustom {
    Notification findByNotificationId(int notificationId);
    void deleteAllByDirectory(Directory directory);
    void deleteAllByBoard(Board board);
    List<Notification> findAllByBoard(Board board);
}
