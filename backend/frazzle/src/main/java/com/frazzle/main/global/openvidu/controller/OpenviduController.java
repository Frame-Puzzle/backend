package com.frazzle.main.global.openvidu.controller;

import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.global.openvidu.dto.OpenviduRequestDto;
import com.frazzle.main.global.openvidu.dto.OpenviduResponseDto;
import com.frazzle.main.global.openvidu.service.OpenViduService;
import com.frazzle.main.global.utils.ResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voice-chats")
@RequiredArgsConstructor
public class OpenviduController {
    private final OpenViduService openViduService;

    @PostMapping
    public ResponseEntity<ResultDto> entryRoom(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody OpenviduRequestDto requestDto) {
        String sessionId = openViduService.createSession(requestDto);

        String tokenId = openViduService.createToken(sessionId);

        OpenviduResponseDto responseDto = OpenviduResponseDto.createOpenviduResponseDto(sessionId, tokenId);

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "세션을 성공적", responseDto));
    }
}
