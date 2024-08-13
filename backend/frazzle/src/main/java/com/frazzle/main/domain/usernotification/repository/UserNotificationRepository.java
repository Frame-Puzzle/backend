package com.frazzle.main.domain.usernotification.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.usernotification.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Integer>, UserNotificationRepositoryCustom {

    Optional<UserNotification> findByUserAndNotification(User user, Notification notification);

    @Override
    void deleteByDirectory(Directory directory);

    @Override
    void deleteByBoard(Board board);

    @Override
    void deleteByDirectoryAndUser(Directory directory, User user);

    void deleteByNotification(Notification notification);

    @Override
    List<UserNotification> findByUserNotNull(User user);

    List<UserNotification> findByUser(User user);
}
