package com.frazzle.main.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenResponseDto {
    private String accessToken;
}
