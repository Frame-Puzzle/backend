package com.frazzle.main.domain.directory.service;

import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.domain.directory.dto.FindMyDirectoryResponseDto;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FindMyDirectoryServiceTest {

    @InjectMocks
    private DirectoryService directoryService;

    @Mock
    private DirectoryRepository directoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPrincipal userPrincipal;

    private User user;
    private List<Directory> responseAll;
    private List<Directory> responseCategory;
    private String category;

    @BeforeEach
    public void setUp() {
        user = User.createUser("1", "싸피", "ssafy@ssafy.com", "google");
        responseAll = new ArrayList<>();
        category = "친구";
        Directory directory = Directory.createDirectory(new CreateDirectoryRequestDto(category, "B208"));
        responseAll.add(directory);
        responseAll.add(Directory.createDirectory(new CreateDirectoryRequestDto("가족", "우리가족")));

        responseCategory = new ArrayList<>();
        responseCategory.add(directory);
    }

    @Test
    @DisplayName("내 전체 디렉토리 조회 성공 테스트")
    public void 내_전체_디렉토리_조회_성공_테스트(){
        //given
        BDDMockito.given(userRepository.findByUserId(userPrincipal.getId())).willReturn(Optional.ofNullable(user));
        BDDMockito.given(directoryRepository.findMyDirectory(user, null)).willReturn(responseAll);

        //when
        List<FindMyDirectoryResponseDto> responses = directoryService.findMyDirectory(userPrincipal, null);

        //then
        Assertions.assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("내 카테고리 디렉토리 조회 성공 테스트")
    public void 내_카테고리_디렉토리_조회_성공_테스트(){
        //given
        BDDMockito.given(userRepository.findByUserId(userPrincipal.getId())).willReturn(Optional.ofNullable(user));
        BDDMockito.given(directoryRepository.findMyDirectory(user, category)).willReturn(responseCategory);

        //when
        List<FindMyDirectoryResponseDto> responses = directoryService.findMyDirectory(userPrincipal, category);

        //then
        Assertions.assertThat(responses.size()).isEqualTo(1);
    }

}
