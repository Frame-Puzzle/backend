package com.frazzle.main.global.openai.dto;

import com.frazzle.main.domain.user.dto.UserInfoResponseDto;
import com.frazzle.main.domain.user.entity.User;
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
