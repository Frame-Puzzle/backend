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

    private User user1;
    private User user2;
    private String loginUserId1;
    private String loginUserId2;
    private List<User> users;

    @BeforeEach
    public void setup(){
        loginUserId1 = "1";
        user1 = User.createUser(loginUserId1,"김싸피", "ssafy@ssafy.com", "kakao");
        loginUserId2 = "2";
        user2 = User.createUser(loginUserId2,"SSafy", "ssafy1@ssafy.com", "kakao");

        users = new ArrayList<>();
        users.add(user2);

    }

    @Test
    @DisplayName("유저 조회 성공 테스트")
    public void 유저_조회_성공_테스트() {
        userService.save(user1);

        User user = userService.findByLoginUserId(loginUserId1);

        Assertions.assertEquals(user, user1);

    }

    @Test
    @DisplayName("유저 삭제 테스트")
    public void 유저_삭제_테스트() {
    }
}
