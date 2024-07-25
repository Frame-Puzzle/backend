package com.frazzle.main.domain.directory.dto;

import com.frazzle.main.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserByEmailResponseDto {

    private int userId;
    private String email;
    private String nickname;
    private String profileUrl;

    private UserByEmailResponseDto(int userId, String email, String nickname, String profileUrl) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }

    public static UserByEmailResponseDto createFindUserByEmailResponseDto(User user) {
        return new UserByEmailResponseDto(user.getUserId(), user.getEmail(), user.getNickname(), user.getProfileImg());
    }
}
