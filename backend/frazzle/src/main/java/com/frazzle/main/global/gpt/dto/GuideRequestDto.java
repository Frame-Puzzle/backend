package com.frazzle.main.global.gpt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GuideRequestDto {

    @NotBlank
    private String[] keywordList;

    @NotBlank
    private int num;

    private String[] preMissionList;
}
