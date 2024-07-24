package com.frazzle.main.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
public class RefreshTokenResponseDto {
    private String accessToken;
}
