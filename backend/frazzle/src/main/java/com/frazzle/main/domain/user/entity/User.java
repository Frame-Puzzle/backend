package com.frazzle.main.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
public class User {

    @Id
    @GeneratedValue
    private int id;

    private String nickname;

    private String email;

    private String socialType;

    private String profileImg;

    private String token;

    private String deviceToken;

    protected User() {}

    public User(String email, String socialType, String token) {
        this.email = email;
        this.socialType = socialType;
        this.token = token;
    }
}
