package com.frazzle.main.domain.directory.service;

import com.frazzle.main.domain.directory.dto.InviteOrCancelMemberRequestDto;
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

import java.util.Optional;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class InviteMemberServiceTest {

    @InjectMocks
    private DirectoryService directoryService;

    @Mock
    private DirectoryRepository directoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDirectoryRepository userDirectoryRepository;

    @Mock
    private UserPrincipal userPrincipal;

    private User user;
    private User member;

    @Mock
    private Directory directory;
    private InviteOrCancelMemberRequestDto requestDto;

    @BeforeEach
    public void setUp() {
        user = User.createUser("1", "김싸피", "ssafy@ssafy.com", "kakao");
        member = User.createUser("2", "싸피", "ssafy@gmail.com", "kakao");
        requestDto = new InviteOrCancelMemberRequestDto(member.getUserId());
    }

    @Test
    @DisplayName("멤버 초대 성공 테스트")
    public void 멤버_초대_성공_테스트() {
        // given
        BDDMockito.given(userPrincipal.getUser()).willReturn(user);
        BDDMockito.given(directoryRepository.findByDirectoryId(directory.getDirectoryId()))
                .willReturn(Optional.of(directory));
        BDDMockito.given(userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(
                        BDDMockito.any(Directory.class),
                        BDDMockito.any(User.class),
                        BDDMockito.eq(true)))
                .willReturn(true);
        BDDMockito.given(userRepository.findByUserId(requestDto.getUserId()))
                .willReturn(Optional.of(member));
        BDDMockito.given(userDirectoryRepository.existsByUserAndDirectory(
                        BDDMockito.any(User.class),
                        BDDMockito.any(Directory.class)))
                .willReturn(false);

        // when
        Assertions.assertThatNoException()
                .isThrownBy(() -> directoryService.inviteMember(userPrincipal, requestDto, directory.getDirectoryId()));

        // then
        BDDMockito.then(directoryRepository)
                .should(times(1)).findByDirectoryId(directory.getDirectoryId());
        BDDMockito.then(userDirectoryRepository)
                .should(times(1)).existsByDirectoryAndUserAndIsAccept(
                        BDDMockito.any(Directory.class),
                        BDDMockito.any(User.class),
                        BDDMockito.eq(true));
        BDDMockito.then(userRepository)
                .should(times(1)).findByUserId(requestDto.getUserId());
        BDDMockito.then(userDirectoryRepository)
                .should(times(1)).existsByUserAndDirectory(
                        BDDMockito.any(User.class),
                        BDDMockito.any(Directory.class));
        BDDMockito.then(userDirectoryRepository)
                .should(times(1)).save(BDDMockito.any(UserDirectory.class));
        BDDMockito.then(directory)
                .should(times(1)).changePeopleNumber(1);
    }

    @Test
    @DisplayName("멤버 초대 권한 없음 실패 테스트")
    public void 멤버_초대_권한_없음_실패_테스트() {
        // given
        BDDMockito.given(userPrincipal.getUser()).willReturn(user);
        BDDMockito.given(directoryRepository.findByDirectoryId(directory.getDirectoryId()))
                .willReturn(Optional.of(directory));
        BDDMockito.given(userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(
                        BDDMockito.any(Directory.class),
                        BDDMockito.any(User.class),
                        BDDMockito.eq(true)))
                .willReturn(false);

        // when
        Assertions.assertThatThrownBy(()->directoryService.inviteMember(userPrincipal, requestDto, directory.getDirectoryId()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.DENIED_INVITE_MEMBER.getMessage());
    }

    @Test
    @DisplayName("멤버 초대 멤버 없음 실패 테스트")
    public void 멤버_초대_멤버_없음_실패_테스트() {
        // given
        BDDMockito.given(userPrincipal.getUser()).willReturn(user);
        BDDMockito.given(directoryRepository.findByDirectoryId(directory.getDirectoryId()))
                .willReturn(Optional.of(directory));
        BDDMockito.given(userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(
                        BDDMockito.any(Directory.class),
                        BDDMockito.any(User.class),
                        BDDMockito.eq(true)))
                .willReturn(true);
        BDDMockito.given(userRepository.findByUserId(requestDto.getUserId()))
                .willReturn(Optional.empty());

        // when
        Assertions.assertThatThrownBy(()->directoryService.inviteMember(userPrincipal, requestDto, directory.getDirectoryId()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.NOT_EXIST_USER.getMessage());
    }

    @Test
    @DisplayName("멤버 초대 중복 실패 테스트")
    public void 멤버_초대_중복_실패_테스트() {
        // given
        BDDMockito.given(userPrincipal.getUser()).willReturn(user);
        BDDMockito.given(directoryRepository.findByDirectoryId(directory.getDirectoryId()))
                .willReturn(Optional.of(directory));
        BDDMockito.given(userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(
                        BDDMockito.any(Directory.class),
                        BDDMockito.any(User.class),
                        BDDMockito.eq(true)))
                .willReturn(true);
        BDDMockito.given(userRepository.findByUserId(requestDto.getUserId()))
                .willReturn(Optional.of(member));
        BDDMockito.given(userDirectoryRepository.existsByUserAndDirectory(
                        BDDMockito.any(User.class),
                        BDDMockito.any(Directory.class)))
                .willReturn(true);

        // when
        Assertions.assertThatThrownBy(()->directoryService.inviteMember(userPrincipal, requestDto, directory.getDirectoryId()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.DUPLICATED_DIRECTORY_MEMBER.getMessage());
    }
}
