package com.frazzle.main.domain.notification.service;

import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.notification.repository.NotificationRepository;
import com.frazzle.main.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<Notification> findAllByUser(User user) {
        return notificationRepository.findByUser(user);
    }
}
