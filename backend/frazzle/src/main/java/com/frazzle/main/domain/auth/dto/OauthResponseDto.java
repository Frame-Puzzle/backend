package com.frazzle.main.domain.auth.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OauthResponseDto {
    private String accessToken;
    private String refreshToken;
}
