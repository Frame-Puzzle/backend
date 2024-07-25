package com.frazzle.main.domain.directory.service;

import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.domain.directory.dto.UpdateDirectoryNameRequestDto;
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

@ExtendWith(MockitoExtension.class)
public class UpdateDirectoryNameServiceTest {

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

    private UpdateDirectoryNameRequestDto requestDto;
    private Directory directory;
    private long loginUserId;
    private User user;
    private UserDirectory userDirectory;

    @BeforeEach
    public void setUp() {
        directory = Directory.createDirectory(new CreateDirectoryRequestDto("친구", "싸피"));
        loginUserId = 1;
        user = User.createUser(loginUserId,"김싸피", "ssafy@ssafy.com", "kakao");
        requestDto = new UpdateDirectoryNameRequestDto("B208");
        userDirectory = UserDirectory.createUserDirectory(directory, user, true);
    }

    @Test
    @DisplayName("디렉토리 이름 수정 성공 테스트")
    public void 디렉토리_이름_수정_성공_테스트(){
        //given
        BDDMockito.given(userPrincipal.getId()).willReturn(loginUserId);
        BDDMockito.given(userRepository.findByLoginUserId(loginUserId)).willReturn(user);
        BDDMockito.given(directoryRepository.findByDirectoryId(directory.getDirectoryId())).willReturn(directory);
        BDDMockito.given(userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)).willReturn(true);

        //when
        Assertions.assertThatNoException().isThrownBy(()->directoryService.updateDirectoryName(userPrincipal, requestDto, directory.getDirectoryId()));

        //then
        BDDMockito.verify(directoryRepository, BDDMockito.times(1)).updateNameByDirectoryId(directory.getDirectoryId(), requestDto.getDirectoryName());
    }

}
