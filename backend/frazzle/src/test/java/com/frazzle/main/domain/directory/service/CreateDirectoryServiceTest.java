package com.frazzle.main.domain.directory.service;

import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
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

@ExtendWith(MockitoExtension.class)
public class CreateDirectoryServiceTest {

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

    private CreateDirectoryRequestDto requestDto;
    private User user;
    private String loginUserId;
    private Directory directory;
    private UserDirectory userDirectory;

    @BeforeEach
    public void setUp() {
        loginUserId = "1";
        user = User.createUser(loginUserId,"김싸피", "ssafy@ssafy.com", "kakao");
        requestDto = new CreateDirectoryRequestDto(
                "친구", "B208"
        );
        directory = Directory.createDirectory(requestDto);
        userDirectory = UserDirectory.createUserDirectory(directory, user, true);
    }

    @Test
    @DisplayName("디렉토리 생성 성공 테스트")
    public void 디렉토리_생성_성공_테스트(){
        //given
        BDDMockito.given(userPrincipal.getId()).willReturn(loginUserId);
        BDDMockito.given(userRepository.findByLoginUserId(loginUserId)).willReturn(user);
        BDDMockito.given(directoryRepository.save(BDDMockito.any(Directory.class))).willReturn(directory);
        BDDMockito.given(userDirectoryRepository.save(BDDMockito.any(UserDirectory.class))).willReturn(userDirectory);

        //when
        Assertions.assertThatNoException().isThrownBy(() -> directoryService.createDirectory(userPrincipal, requestDto));

        //then
        BDDMockito.verify(directoryRepository, BDDMockito.times(1)).save(BDDMockito.any(Directory.class));
        BDDMockito.verify(userDirectoryRepository, BDDMockito.times(1)).save(BDDMockito.any(UserDirectory.class));
    }
}
