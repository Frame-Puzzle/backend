package com.frazzle.main.domain.notification.dto;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.usernotification.entity.UserNotification;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationResponseDto {
    private LocalDateTime createTime;
    private String type;
    private String directoryName;
    private String category;
    private String createUserName;
    private Boolean isRead;
    private int acceptStatus;

    @Builder
    private NotificationResponseDto(LocalDateTime createTime, String type, String directoryName, String category, String createUserName, Boolean isRead, int acceptStatus) {
        this.createTime = createTime;
        this.type = type;
        this.directoryName = directoryName;
        this.category = category;
        this.createUserName = createUserName;
        this.isRead = isRead;
        this.acceptStatus = acceptStatus;
    }

    public static NotificationResponseDto createNotificationResponse(UserNotification userNotification) {
        Notification notification = userNotification.getNotification();
        Directory directory = notification.getDirectory();

        return NotificationResponseDto.builder()
                .createTime(notification.getCreatedAt())
                .type(notification.getType())
                .directoryName(directory.getDirectoryName())
                .category(directory.getCategory())
                .createUserName(userNotification.getUser().getNickname())
                .isRead(userNotification.getIsRead())
                .acceptStatus(userNotification.getAcceptStatus())
                .build();

    }
}

/*
						{
								"createTime":"2024-07-19",
								"type":1,
								"directoryName": "디렉토리 명",
								"category":"친구"
								"createUserName": "유저명",
								"isRead":false,
								"acceptStatus":0
						},

 */