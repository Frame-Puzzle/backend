package com.frazzle.main.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    private Long loginUserId;

    private String nickname;

    private String email;

    private String socialType;

    private String profileImg;

    private String refreshToken;

    private String deviceToken;

    protected User() {}

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
