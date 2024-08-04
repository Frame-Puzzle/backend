package com.frazzle.main.global.openai.controller;

import com.frazzle.main.global.openai.dto.GuideRequestDto;
import com.frazzle.main.global.openai.dto.GuideResponseDto;
import com.frazzle.main.global.openai.service.GuideService;
import com.frazzle.main.global.utils.ResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    //보드id로
    @Operation(summary = "미션 생성", description = "ChatGPT를 활용한 미션 생성하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "생성에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @PostMapping("/guides")
    public ResponseEntity<ResultDto> describeImage(@RequestBody GuideRequestDto requestDto) {

        String[] guideList = gptService.generateDescription(requestDto);

        GuideResponseDto responseDto = GuideResponseDto.createGuideResponseDto(guideList);

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "미션 생성하는데 성공했습니다.", responseDto));

    }
}
