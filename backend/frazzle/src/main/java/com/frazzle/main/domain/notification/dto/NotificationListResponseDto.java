package com.frazzle.main.domain.notification.dto;

import com.frazzle.main.domain.notification.entity.Notification;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NotificationListResponseDto {
    List<Notification> notificationList;

    public static NotificationListResponseDto createResponseDto(List<Notification> notificationList) {
        return NotificationListResponseDto.builder()
                .notificationList(notificationList).build();
    }
}
