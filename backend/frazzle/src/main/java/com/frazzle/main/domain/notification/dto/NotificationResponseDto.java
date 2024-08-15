package com.frazzle.main.domain.notification.dto;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.usernotification.entity.UserNotification;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
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
    private int directoryId;


    @Builder
    private NotificationResponseDto(int notificationId, LocalDateTime createTime, int type, String directoryName, String category, String createUserName, Boolean isRead, int acceptStatus, int boardId, String profileImg, int boardNum, int directoryId) {
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
        this.directoryId = directoryId;
    }


    public static NotificationResponseDto createNotificationResponse(UserNotification userNotification) {
        Notification notification = userNotification.getNotification();
        Directory directory = notification.getDirectory();
        User user = notification.getUser();

        String profileImg = (user != null) ? user.getProfileImg() : null;
        String nickname = (user != null) ? user.getNickname() : null;

        if(notification.getBoard() != null) {
            return NotificationResponseDto.builder()
                    .notificationId(notification.getNotificationId())
                    .createTime(notification.getCreatedAt())
                    .type(notification.getType())
                    .directoryName(directory.getDirectoryName())
                    .category(directory.getCategory())
                    .createUserName(nickname)
                    .isRead(userNotification.getIsRead())
                    .acceptStatus(userNotification.getAcceptStatus())
                    .boardId(notification.getBoard().getBoardId())
                    .boardNum(notification.getBoard().getBoardInNumber())
                    .profileImg(notification.getUser().getProfileImg())
                    .directoryId(directory.getDirectoryId())
                    .build();
        }

        return NotificationResponseDto.builder()
                .notificationId(notification.getNotificationId())
                .createTime(notification.getCreatedAt())
                .type(notification.getType())
                .directoryName(directory.getDirectoryName())
                .category(directory.getCategory())
                .createUserName(nickname)
                .isRead(userNotification.getIsRead())
                .acceptStatus(userNotification.getAcceptStatus())
                .boardId(0)
                .profileImg(profileImg)
                .build();

    }
}