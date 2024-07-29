package com.frazzle.main.domain.directory.service;

import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.global.models.UserPrincipal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPrincipal userPrincipal;

    private User user;
    private int userId;

    @BeforeEach
    public void setup(){
        userId = 1;
        user = User.createUser("1","김싸피", "ssafy@ssafy.com", "kakao");
    }

    @Test
    @DisplayName("유저 조회 성공 테스트")
    public void 유저_조회_성공_테스트() {
        userService.save(user);

        User user = userService.findByUserId(userId);

        Assertions.assertEquals(user, user);

    }

    @Test
    @DisplayName("유저 삭제 테스트")
    public void 유저_삭제_테스트() {
    }
}
