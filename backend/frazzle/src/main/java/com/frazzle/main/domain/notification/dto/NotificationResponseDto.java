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
    private int notificationId;
    private LocalDateTime createTime;
    private int type;
    private String directoryName;
    private String category;
    private String createUserName;
    private Boolean isRead;
    private int acceptStatus;
    private int boardId;
    private String profileImg;
    private int boardNum;

    @Builder
    public NotificationResponseDto(int notificationId, LocalDateTime createTime, int type, String directoryName, String category, String createUserName, Boolean isRead, int acceptStatus, int boardId, String profileImg, int boardNum) {
        this.notificationId = notificationId;
        this.createTime = createTime;
        this.type = type;
        this.directoryName = directoryName;
        this.category = category;
        this.createUserName = createUserName;
        this.isRead = isRead;
        this.acceptStatus = acceptStatus;
        this.boardId = boardId;
        this.profileImg = profileImg;
        this.boardNum = boardNum;
    }


    public static NotificationResponseDto createNotificationResponse(UserNotification userNotification) {
        Notification notification = userNotification.getNotification();
        Directory directory = notification.getDirectory();

        String profileImg = (notification.getUser() != null) ? notification.getUser().getProfileImg() : null;

        if(notification.getBoard() != null) {
            return NotificationResponseDto.builder()
                    .notificationId(notification.getNotificationId())
                    .createTime(notification.getCreatedAt())
                    .type(notification.getType())
                    .directoryName(directory.getDirectoryName())
                    .category(directory.getCategory())
                    .createUserName(userNotification.getUser().getNickname())
                    .isRead(userNotification.getIsRead())
                    .acceptStatus(userNotification.getAcceptStatus())
                    .boardId(notification.getBoard().getBoardId())
                    .boardNum(notification.getBoard().getBoardInNumber())
                    .profileImg(profileImg)
                    .build();
        }

        return NotificationResponseDto.builder()
                .notificationId(notification.getNotificationId())
                .createTime(notification.getCreatedAt())
                .type(notification.getType())
                .directoryName(directory.getDirectoryName())
                .category(directory.getCategory())
                .createUserName(userNotification.getUser().getNickname())
                .isRead(userNotification.getIsRead())
                .acceptStatus(userNotification.getAcceptStatus())
                .boardId(0)
                .profileImg(profileImg)
                .build();

    }
}