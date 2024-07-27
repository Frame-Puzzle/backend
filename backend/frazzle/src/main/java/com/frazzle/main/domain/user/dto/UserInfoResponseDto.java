package com.frazzle.main.domain.user.dto;

import com.frazzle.main.domain.user.entity.User;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class UserInfoResponseDto {
    private int userId;
    private String nickname;
    private String email;
    private String profileImg;

    public static UserInfoResponseDto createUserInfoResponse(User user) {
        return new UserInfoResponseDto(user.getUserId(), user.getNickname(), user.getEmail(), user.getProfileImg());
    }
}
