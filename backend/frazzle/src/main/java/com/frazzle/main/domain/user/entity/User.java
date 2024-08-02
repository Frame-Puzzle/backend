package com.frazzle.main.domain.user.entity;

import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "login_user_id", nullable = false, unique = true)
    private String loginUserId;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "email", length = 64, nullable = false, unique = true)
    private String email;

    @Column(name = "social_type", length = 10, nullable = false)
    private String socialType;

    @Column(name = "profile_img", unique = true)
    private String profileImg;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "device_token")
    private String deviceToken;

    @PrePersist
    @PreUpdate
    private void validateNickname() {
        if (nickname != null) {
            int length = 0;
            int maxLength = 20;

            for (char c : nickname.toCharArray()) {
                if (c >= 0xAC00 && c <= 0xD7A3) {
                    // 한글은 2 길이로 계산
                    length += 2;
                } else {
                    // 그 외의 문자는 1 길이로 계산
                    length += 1;
                }

                if (length > maxLength) {
                    throw new CustomException(ErrorCode.TOO_MAX_LENGTH_NICKNAME);
                }
            }
        }
    }

    @Builder
    private User(String loginUserId, String nickname, String email, String socialType, String profileImg, String refreshToken, String deviceToken) {
        this.loginUserId = loginUserId;
        this.nickname = nickname;
        this.email = email;
        this.socialType = socialType;
        this.profileImg = profileImg;
        this.refreshToken = refreshToken;
        this.deviceToken = deviceToken;
    }

    public static User createUser(String loginUserId, String nickname, String email, String socialType) {
        return User.builder()
                .loginUserId(loginUserId)
                .nickname(nickname)
                .email(email)
                .socialType(socialType)
                .build();
    }
    public void updateUserProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void updateUserNickname(String nickname) {
        this.nickname = nickname;
    }
}
