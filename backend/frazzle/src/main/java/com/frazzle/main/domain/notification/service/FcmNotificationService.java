package com.frazzle.main.domain.notification.service;

import com.frazzle.main.domain.notification.dto.FcmNotificationRequestDto;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FcmNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    public void sendNotification(FcmNotificationRequestDto fcmNotification) {
        Optional<User> user = userRepository.findById(fcmNotification.getTargetUserId());

        user.ifPresent(u -> {
            if(u.getDeviceToken()==null) {
                throw new CustomException(ErrorCode.NOT_EXIST_FIREBASE_TOKEN);
            }

            Notification notification = Notification.builder()
                    .setTitle(fcmNotification.getTitle())
                    .setBody(fcmNotification.getBody())
                    .build();

            Message message = Message.builder()
                    .setToken(u.getDeviceToken())
                    .setNotification(notification)
                    .build();

            try {
                firebaseMessaging.send(message);
            } catch (FirebaseMessagingException e) {
                throw new CustomException(ErrorCode.FAILED_SEND_MESSAGE);
            }
        });
    }
}
