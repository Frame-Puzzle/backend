package com.frazzle.main.domain.user.dto;

import com.frazzle.main.domain.user.validation.annotation.AtLeastOneNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@AtLeastOneNotBlank //적어도 하나의 값은 존재해야함
public class UpdateUserRequestDto {
    private String nickname;
    private String profileImg;
}