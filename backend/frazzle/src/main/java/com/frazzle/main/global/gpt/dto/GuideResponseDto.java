package com.frazzle.main.global.gpt.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GuideResponseDto {
    private String[] guideList;

    public static GuideResponseDto createGuideResponseDto(String[] guideList) {
        return new GuideResponseDto(guideList);
    }
}
