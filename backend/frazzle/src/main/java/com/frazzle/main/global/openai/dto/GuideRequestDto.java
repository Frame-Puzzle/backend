package com.frazzle.main.global.openai.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GuideRequestDto {
    private String[] keywordList;
    private int num;
    private String[] preMissionList;
}
