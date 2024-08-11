package com.frazzle.main.domain.socket.roby.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class EntryRobyRequestDto {

    @NotNull
    int boardId;

    @NotBlank
    private int userId;

    @NotBlank
    private String message;

    @NotBlank
    private String nickname;

    @NotBlank
    private int size;



    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeUserId(int userId) {
        this.userId = userId;
    };

    public void entryMessage(String message) {
        this.message = message;
    }
}
