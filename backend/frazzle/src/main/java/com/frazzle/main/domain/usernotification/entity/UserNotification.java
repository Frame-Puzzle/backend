package com.frazzle.main.domain.usernotification.entity;

import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_notifications", uniqueConstraints = {@UniqueConstraint(
        name = "USER_NOTIFICATION_UNIQUE",
        columnNames = {"notification_id", "user_id"}
)})
//notification_id와 user_id가 같이 동일한 값이 중복되지 않도록 제약 조건 설정
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_notification_id")
    private int userNotificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "accept_status")
    private String acceptStatus;

    @Builder
    private UserNotification(User user, Notification notification, Boolean isRead, String acceptStatus) {
        this.user = user;
        this.notification = notification;
        this.isRead = isRead;
        this.acceptStatus = acceptStatus;
    }

    public static UserNotification createUserNotification(User user, Notification notification, Boolean isRead, String acceptStatus) {
        return UserNotification.builder()
                .user(user)
                .notification(notification)
                .isRead(isRead)
                .acceptStatus(acceptStatus)
                .build();
    }
}
