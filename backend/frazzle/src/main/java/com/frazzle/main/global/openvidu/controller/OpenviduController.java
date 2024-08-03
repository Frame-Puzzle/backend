package com.frazzle.main.global.openvidu.controller;

import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.global.openvidu.dto.OpenviduRequestDto;
import com.frazzle.main.global.openvidu.dto.OpenviduResponseDto;
import com.frazzle.main.global.openvidu.service.OpenViduService;
import com.frazzle.main.global.utils.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voice-chats")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class OpenviduController {
    private final OpenViduService openViduService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResultDto> entryChat(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody OpenviduRequestDto requestDto) {

        log.info("entryChat");
        log.info(String.valueOf(requestDto.getBoardId()));
        User user = userService.findByUserId(userPrincipal);
        log.info(String.valueOf(user.getUserId()));


        OpenviduResponseDto responseDto = openViduService.entryChat(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "세션과 토큰을 성공적으로 생성했습니다.", responseDto));
    }
}
