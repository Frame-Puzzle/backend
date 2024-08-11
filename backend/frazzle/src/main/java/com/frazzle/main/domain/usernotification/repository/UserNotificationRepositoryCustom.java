package com.frazzle.main.domain.usernotification.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.user.entity.User;

import java.util.List;

public interface UserNotificationRepositoryCustom {

    void deleteByDirectory(Directory directory);
    void deleteByDirectoryAndUser(Directory directory, User user);
    void deleteByBoard(Board board);
    void updateCancelUserNotification(int notificationId);
    void updateMemberCancel(User user, Directory directory);
}
