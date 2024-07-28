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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
<<<<<<< backend/frazzle/src/main/java/com/frazzle/main/domain/user/controller/UserController.java
=======
    @Operation(summary = "유저 정보 조회", description = "로그인한 유저의 정보를 조회합니다.")
    //가능한 엔드포인트와 설명
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 조회에 성공했습니다.",
                    //응답 본문의 구조를 설명
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
>>>>>>> backend/frazzle/src/main/java/com/frazzle/main/domain/user/controller/UserController.java
    @GetMapping
    public ResponseEntity<ResultDto> userInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        log.info("user "+String.valueOf(userPrincipal.getId()));

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
<<<<<<< backend/frazzle/src/main/java/com/frazzle/main/domain/user/controller/UserController.java
=======
    @Operation(summary = "유저 삭제", description = "로그인한 유저를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴가 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
>>>>>>> backend/frazzle/src/main/java/com/frazzle/main/domain/user/controller/UserController.java
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
<<<<<<< backend/frazzle/src/main/java/com/frazzle/main/domain/user/controller/UserController.java
=======
    @Operation(summary = "유저 정보 수정", description = "로그인한 유저의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 수정에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
>>>>>>> backend/frazzle/src/main/java/com/frazzle/main/domain/user/controller/UserController.java
    @PutMapping
    public ResponseEntity<ResultDto> updateUser(@AuthenticationPrincipal UserPrincipal userPrincipal, @Validated @RequestBody UpdateUserRequestDto updateUserRequestDto) {

        int userId = userPrincipal.getId();

        User user = userService.findByUserId(userId);

        log.info("before "+user.getNickname());

        Long result = userService.updateUserByNicknameOrImg(user, updateUserRequestDto);

        user = userService.findByUserId(userId);
        log.info("after "+user.getNickname());

        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.createUserInfoResponse(user);

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "유저 정보 수정에 성공했습니다.", userInfoResponseDto));
    }

    //닉네임 찾기
<<<<<<< backend/frazzle/src/main/java/com/frazzle/main/domain/user/controller/UserController.java
=======
    @Operation(summary = "닉네임 중복 체크", description = "닉네임의 중복 여부를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 조회에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
>>>>>>> backend/frazzle/src/main/java/com/frazzle/main/domain/user/controller/UserController.java
    @GetMapping("/find")
    public ResponseEntity<ResultDto> checkNickname(@RequestParam("nickname") String nickname) {
        Boolean isExist = userService.findByNickname(nickname);

        ExistNicknameResponseDto existNicknameResponseDto = ExistNicknameResponseDto.createExistNickameResponseDto(isExist);

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "닉네임 조회를 성공했습니다.", existNicknameResponseDto));
    }

}


