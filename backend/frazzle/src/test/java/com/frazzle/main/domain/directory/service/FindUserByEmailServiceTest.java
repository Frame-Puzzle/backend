package com.frazzle.main.domain.directory.service;

import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.domain.directory.dto.UserByEmailResponseDto;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
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
    private UserDirectory userDirectory;
    private User user1;
    private User user2;
    private long loginUserId1;
    private List<User> users;

    @BeforeEach
    public void setup(){
        loginUserId1 = 1;
        user1 = User.createUser(loginUserId1,"김싸피", "ssafy@ssafy.com", "kakao");

        user2 = User.createUser(2L,"SSafy", "ssafy1@ssafy.com", "kakao");

        users = new ArrayList<>();
        users.add(user2);

        directory = Directory.createDirectory(new CreateDirectoryRequestDto("친구", "B208"));
        userDirectory = UserDirectory.createUserDirectory(directory, user1, true);

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
}
