package com.frazzle.main.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "login_user_id", nullable = false)
    private String loginUserId;

    @Column(name = "nickname", length = 32, nullable = false)
    private String nickname;

    @Column(name = "email", length = 64, nullable = false)
    private String email;

    @Column(name = "social_type", length = 10, nullable = false)
    private String socialType;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "device_token")
    private String deviceToken;

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

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static User createUser(String loginUserId, String nickname, String email, String socialType) {
        return User.builder()
                .loginUserId(loginUserId)
                .nickname(nickname)
                .email(email)
                .socialType(socialType)
                .build();
    }
}
