package com.frazzle.main.domain.notification.service;

import com.frazzle.main.domain.notification.dto.AcceptNotificationRequestDto;
import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.notification.repository.NotificationRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.usernotification.entity.UserNotification;
import com.frazzle.main.domain.usernotification.repository.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;

    public List<Notification> findAllByUser(User user) {
        return notificationRepository.findByUser(user);
    }

    public void updateUserNotification(User user, int notificationId, AcceptNotificationRequestDto requestDto) {
        Notification notification = notificationRepository.findByNotificationId(notificationId);

        UserNotification userNotification = userNotificationRepository.findByUserAndNotification(user, notification);

        userNotification.updateRead();

        userNotification.updateStatus(requestDto.getAccept());

    }
}
