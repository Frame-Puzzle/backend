package com.frazzle.main.domain.user.controller;

import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<User> userInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        log.info(String.valueOf(userPrincipal.getId()));

        String loginUserId = userPrincipal.getId();

        User user = userService.findByLoginUserId(loginUserId);
        if(user == null) {
            throw new CustomException(ErrorCode.NOT_EXIST_USER);
        }

        return ResponseEntity.ok(user);
    }

}


