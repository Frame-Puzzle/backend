package com.frazzle.main.domain.user.controller;

import com.frazzle.main.global.utils.SecurityUtil;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<User> userInfo() {
        final long loginUserId = SecurityUtil.getCurrentUserId();

        User user = userService.findByLoginUserId(loginUserId);
        if(user == null) {
            throw new CustomException(ErrorCode.NOT_EXIST_USER);
        }

        return ResponseEntity.ok(user);
    }

}


