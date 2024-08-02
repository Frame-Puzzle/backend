package com.frazzle.main.global.openai.controller;

import com.frazzle.main.global.openai.dto.GuideRequestDto;
import com.frazzle.main.global.openai.dto.GuideResponseDto;
import com.frazzle.main.global.openai.service.GuideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//추후에 퍼즐판 생성으로 넘어감
@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class GuideController {

    private final GuideService gptService;

    //추후에 퍼즐판 생성으로 넘어감
    @PostMapping("/guides")
    public GuideResponseDto describeImage(@RequestBody GuideRequestDto requestDto) {

        String[] guideList = gptService.generateDescription(requestDto);

        return GuideResponseDto.createGuideResponseDto(guideList);
    }
}
