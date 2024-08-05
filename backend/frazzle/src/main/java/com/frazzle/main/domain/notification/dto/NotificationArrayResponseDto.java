package com.frazzle.main.domain.notification.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationArrayResponseDto {
    NotificationResponseDto[] notificationList;


    @Builder
    private NotificationArrayResponseDto(NotificationResponseDto[] notificationList) {
        this.notificationList = notificationList;
    }

    public static NotificationArrayResponseDto createArrayDto(NotificationResponseDto[] notificationList) {
        return NotificationArrayResponseDto.builder()
                .notificationList(notificationList)
                .build();
    }
}
