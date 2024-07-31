package com.frazzle.main.domain.user.controller;

import com.frazzle.main.domain.user.dto.*;
import com.frazzle.main.global.aws.service.AwsService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final AwsService awsService;

    //유저 찾기
    @Operation(summary = "유저 정보 조회", description = "로그인한 유저의 정보를 조회합니다.")
    //가능한 엔드포인트와 설명
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 조회에 성공했습니다.",
                    //응답 본문의 구조를 설명
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
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
    @Operation(summary = "유저 삭제", description = "로그인한 유저를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴가 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @DeleteMapping
    public ResponseEntity<ResultDto> deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        int userId = userPrincipal.getId();

        userService.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "회원 탈퇴가 성공했습니다."));
    }

    //유저 정보 업데이트
    @Operation(summary = "유저 닉네임 정보 수정", description = "로그인한 유저의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 수정에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @PutMapping("/nickname")
    public ResponseEntity<ResultDto> updateUserNickname(@AuthenticationPrincipal UserPrincipal userPrincipal, UpdateUserNicknameRequestDto requestDto) {

        int userId = userPrincipal.getId();

        User user = userService.findByUserId(userId);

        //만약 닉네임 변경시 여기서 발생
        user = userService.updateUserByNickname(user, requestDto);

        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.createUserInfoResponse(user);

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "유저 닉네임 정보 수정에 성공했습니다.", userInfoResponseDto));
    }

    //유저 정보 업데이트
    @Operation(summary = "유저 프로필 정보 수정", description = "로그인한 유저의 프로필 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 수정에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @PutMapping("/profile-img")
    public ResponseEntity<ResultDto> updateUserProfileImg(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody MultipartFile profileImg) {

        int userId = userPrincipal.getId();

        User user = userService.findByUserId(userId);
        log.info(profileImg.toString());

        //사진 업로드 후 유저url 반환
        String userUrl = awsService.uploadFile(profileImg, user.getLoginUserId());

        //유저url을 통해 S3에서 이미지 가져오기
        String url = awsService.getProfileUrl(userUrl);


        //가져온 실제 url을 db에 url 저장
        user = userService.updateUserByProfileImg(user, url);

        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.createUserInfoResponse(user);

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "유저 프로필 정보 수정에 성공했습니다.", userInfoResponseDto));
    }

    //닉네임 찾기
    @Operation(summary = "닉네임 중복 체크", description = "닉네임의 중복 여부를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 조회에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @GetMapping("/find")
    public ResponseEntity<ResultDto> checkNickname(@RequestParam("nickname") String nickname) {
        Boolean isExist = userService.findByNickname(nickname);

        ExistNicknameResponseDto existNicknameResponseDto = ExistNicknameResponseDto.createExistNickameResponseDto(isExist);

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "닉네임 조회를 성공했습니다.", existNicknameResponseDto));
    }

}


