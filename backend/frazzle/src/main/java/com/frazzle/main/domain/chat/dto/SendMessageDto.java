package com.frazzle.main.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SendMessageDto {

    @NotNull
    int roomId;

    @NotBlank
    private String nickname;

    @NotBlank
    private String message;

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    };

    public void entryMessage(String message) {
        this.message = message;
    }
}
