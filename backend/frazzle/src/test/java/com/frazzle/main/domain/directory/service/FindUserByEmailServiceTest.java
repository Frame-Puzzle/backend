package com.frazzle.main.domain.directory.service;

import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.domain.directory.dto.UserByEmailResponseDto;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FindUserByEmailServiceTest {

    @InjectMocks
    private DirectoryService directoryService;

    @Mock
    private DirectoryRepository directoryRepository;

    @Mock
    private UserDirectoryRepository userDirectoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPrincipal userPrincipal;

    private String email;
    private Directory directory;
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

        directory = Directory.createDirectory(new CreateDirectoryRequestDto("친구", "B208"));

        email = "s";
    }

    @Test
    @DisplayName("이메일로 멤버 정보 조회 성공 테스트")
    public void 이메일로_멤버_정보_조회_성공_테스트(){
        //given
        BDDMockito.given(userPrincipal.getId()).willReturn(loginUserId1);
        BDDMockito.given(userRepository.findByLoginUserId(loginUserId1)).willReturn(user1);
        BDDMockito.given(directoryRepository.findByDirectoryId(directory.getDirectoryId())).willReturn(directory);
        BDDMockito.given(userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user1, true)).willReturn(true);
        BDDMockito.given(userRepository.findUsersByEmail(email, directory)).willReturn(users);

        //when
        List<UserByEmailResponseDto> responses = directoryService.findUserByEmail(userPrincipal, email, directory.getDirectoryId());

        //then
        Assertions.assertThat(responses.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("이메일로 멤버 정보 조회 권한 없음 실패 테스트")
    public void 이메일로_멤버_정보_조회_권한_없음_실패_테스트(){
        //given
        BDDMockito.given(userPrincipal.getId()).willReturn(loginUserId2);
        BDDMockito.given(userRepository.findByLoginUserId(loginUserId2)).willReturn(user2);
        BDDMockito.given(directoryRepository.findByDirectoryId(directory.getDirectoryId())).willReturn(directory);
        BDDMockito.given(userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user2, true)).willReturn(false);

        Assertions.assertThatThrownBy(() -> directoryService.findUserByEmail(userPrincipal, email, directory.getDirectoryId()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.DENIED_FIND_MEMBER.getMessage());

        //then
        BDDMockito.then(userDirectoryRepository).should().existsByDirectoryAndUserAndIsAccept(directory, user2, true);
    }
}
