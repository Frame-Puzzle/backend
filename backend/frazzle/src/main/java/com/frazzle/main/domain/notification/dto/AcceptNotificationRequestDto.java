package com.frazzle.main.domain.notification.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AcceptNotificationRequestDto {
    @NotBlank
    private Boolean isRead;
    @NotBlank
    private String accept;
}
