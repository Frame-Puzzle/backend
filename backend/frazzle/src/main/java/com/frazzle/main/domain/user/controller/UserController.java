package com.frazzle.main.domain.user.controller;

import com.frazzle.main.domain.user.dto.ExistNicknameResponseDto;
import com.frazzle.main.domain.user.dto.UpdateUserRequestDto;
import com.frazzle.main.domain.user.dto.UserInfoResponseDto;
import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.utils.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    //유저 찾기
    @GetMapping
    public ResponseEntity<ResultDto> userInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        log.info(String.valueOf(userPrincipal.getId()));

        int userId = userPrincipal.getId();

        User user = userService.findByUserId(userId);
        if(user == null) {
            throw new CustomException(ErrorCode.NOT_EXIST_USER);
        }

        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.createUserInfoResponse(user);

        log.info(userInfoResponseDto.toString());

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "유저 정보 조회에 성공했습니다.", userInfoResponseDto));
    }

    //유저 삭제
    @DeleteMapping
    public ResponseEntity<ResultDto> deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        int userId = userPrincipal.getId();

        Long result = userService.deleteUser(userId);

        if(result > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "회원 탈퇴가 성공했습니다."));
        }
        else {
            throw new CustomException(ErrorCode.NOT_EXIST_USER);
        }

    }

    //유저 정보 업데이트
    @PutMapping
    public ResponseEntity<ResultDto> updateUser(@AuthenticationPrincipal UserPrincipal userPrincipal, @Validated @RequestBody UpdateUserRequestDto updateUserRequestDto) {

        int userId = userPrincipal.getId();

        User user = userService.findByUserId(userId);

        Long result = userService.updateUserByNicknameOrImg(user, updateUserRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "유저 정보 수정에 성공했습니다."));
    }

    //닉네임 찾기
    @GetMapping("/find")
    public ResponseEntity<ResultDto> checkNickname(@RequestParam("nickname") String nickname) {
        Boolean isExist = userService.findByNickname(nickname);

        ExistNicknameResponseDto existNicknameResponseDto = ExistNicknameResponseDto.createExistNickameResponseDto(isExist);

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "닉네임 조회를 성공했습니다.", existNicknameResponseDto));
    }

}


