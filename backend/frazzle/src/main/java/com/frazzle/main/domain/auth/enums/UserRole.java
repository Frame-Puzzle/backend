package com.frazzle.main.domain.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {
    USER("user"),
    ADMIN("admin");

    private final String role;
}
