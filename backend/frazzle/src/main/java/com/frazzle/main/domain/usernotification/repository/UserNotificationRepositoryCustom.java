package com.frazzle.main.domain.usernotification.repository;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.notification.entity.Notification;

import java.util.List;

public interface UserNotificationRepositoryCustom {

    void deleteByDirectory(Directory directory);

    void deleteByBoard(Board board);
}
