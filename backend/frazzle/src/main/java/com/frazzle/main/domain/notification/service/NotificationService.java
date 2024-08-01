package com.frazzle.main.domain.notification.service;

import com.frazzle.main.domain.notification.dto.AcceptNotificationRequestDto;
import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.notification.repository.NotificationRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.usernotification.entity.UserNotification;
import com.frazzle.main.domain.usernotification.repository.UserNotificationRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<Notification> findAllByUser(UserPrincipal userPrincipal) {
        //1. 유저 정보 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        return notificationRepository.findByUser(user);
    }

    @Transactional
    public void updateUserNotification(UserPrincipal userPrincipal, int notificationId, AcceptNotificationRequestDto requestDto) {
        //1. 유저 정보 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        Notification notification = notificationRepository.findByNotificationId(notificationId);

        UserNotification userNotification = userNotificationRepository.findByUserAndNotification(user, notification);

        userNotification.updateRead();

        userNotification.updateStatus(requestDto.getAccept());

    }
}
