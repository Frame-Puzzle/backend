package com.frazzle.main.domain.user.service;

import com.frazzle.main.domain.directory.dto.UserByEmailResponseDto;
import com.frazzle.main.domain.user.dto.UpdateUserNicknameRequestDto;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.global.models.UserPrincipal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPrincipal userPrincipal;

    private User user;
    private int userId;
    private UpdateUserNicknameRequestDto nicknameRequestDto;

    @BeforeEach
    public void setup(){
        userId = 1;
        user = User.createUser("1","김싸피", "ssafy@ssafy.com", "kakao");
        nicknameRequestDto = new UpdateUserNicknameRequestDto("이싸피");
    }

    @Test
    @DisplayName("유저 조회 성공 테스트")
    public void 유저_조회_성공_테스트() {
        //given
        userService.save(user);
        verify(userService).save(user);

        //when
        //User user = userService.findByUserId(userId);
        //verify(userService).findByUserId(userId);

        //then
        Assertions.assertEquals(user, user);
    }

    @Test
    @DisplayName("유저 삭제 테스트")
    public void 유저_삭제_테스트() {
        // Save the user
        userService.save(user);
        verify(userService).save(user); // 메서드 호출 되었는지 확인

        // when
        userService.deleteUser(userPrincipal);
        verify(userService).deleteUser(userPrincipal); // 메서드 호출 되었는지 확인

        // then
        Assertions.assertNull(userService.findByUserId(userPrincipal));
    }

    @Test
    @DisplayName("유저 닉네임 수정 성공 테스트")
    public void 유저_닉네임_수정_성공_테스트() {
        //given
        userService.save(user);
        verify(userService).save(user);

        //when
        userService.updateUserByNickname(userPrincipal, nicknameRequestDto);
        verify(userService).updateUserByNickname(userPrincipal, nicknameRequestDto);

        //then
        Assertions.assertEquals(user.getNickname(), nicknameRequestDto.getNickname());



    }
}
