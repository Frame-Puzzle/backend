package com.frazzle.main.domain.notification.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmNotificationRequestDto {
    private int targetUserId;
    private String title;
    private String body;

    @Builder
    public FcmNotificationRequestDto(int targetUserId, String title, String body) {
        this.targetUserId = targetUserId;
        this.title = title;
        this.body = body;
    }

    public static FcmNotificationRequestDto createFcmNotification(int targetUserId, String title, String body) {
        return FcmNotificationRequestDto.builder()
                .targetUserId(targetUserId)
                .title(title)
                .body(body)
                .build();
    }

}
