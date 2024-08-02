package com.frazzle.main.domain.piece.controller;

import com.frazzle.main.domain.user.dto.UpdateUserNicknameRequestDto;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PieceControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @AfterEach
    void tearDown() {
    }
    @BeforeEach
    void setUp() {
        User user = User.createUser("1","김싸피", "ssafy@ssafy.com", "kakao");
        userRepository.save(user);
    }

    @Test
    void updatePiece() {

        int a = 0;
    }

    @Test
    void detailPiece() {
    }
}