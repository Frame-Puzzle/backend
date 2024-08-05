package com.frazzle.main.domain.notification.controller;

import com.frazzle.main.domain.notification.dto.AcceptNotificationRequestDto;
import com.frazzle.main.domain.notification.dto.FcmNotificationRequestDto;
import com.frazzle.main.domain.notification.dto.NotificationArrayResponseDto;
import com.frazzle.main.domain.notification.dto.NotificationResponseDto;
import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.notification.service.FcmNotificationService;
import com.frazzle.main.domain.notification.service.NotificationService;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.domain.usernotification.entity.UserNotification;
import com.frazzle.main.domain.usernotification.repository.UserNotificationRepository;
import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.global.utils.ResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;
    private final FcmNotificationService fcmNotificationService;

    @Operation(summary = "알림 전체 조회", description = "사용자의 알림을 전체 조회 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "조회에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @GetMapping
    public ResponseEntity<ResultDto> getNotifications(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        //유저의 알림 모두 가져오기
        List<UserNotification> userNotificationList = notificationService.findAllByUser(userPrincipal);

//        fcmNotificationService.sendNotification(FcmNotificationRequestDto.createFcmNotification(userPrincipal.getId(),"제목","바디"));

        List<NotificationResponseDto> notificationList = new ArrayList<>();

        //가져온 유저의 알림에서 필요한 것들을 저장
        for(UserNotification userNotification : userNotificationList) {
            NotificationResponseDto responseDto = NotificationResponseDto.createNotificationResponse(userNotification);
            notificationList.add(responseDto);
        }

        //최신 순으로 정렬
        NotificationArrayResponseDto responseDto = NotificationArrayResponseDto.createArrayDto(
                notificationList.stream()
                        .sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()))
                        .toArray(NotificationResponseDto[]::new)
        );

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "알림 전체 조회가 성공했습니다.", responseDto));
    }


    @Operation(summary = "알림 읽음, 수락", description = "사용자가 알림을 읽음 또는 수락합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "조회에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @PutMapping("/{notificationId}")
    public ResponseEntity<ResultDto> acceptNotification(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("notificationId") int notificationId,
            @Valid @RequestBody AcceptNotificationRequestDto requestDto) {

        notificationService.updateUserNotification(userPrincipal, notificationId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "알림 전체 조회가 성공했습니다."));
    }



}
