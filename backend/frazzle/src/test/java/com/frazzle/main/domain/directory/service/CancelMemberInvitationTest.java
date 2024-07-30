package com.frazzle.main.domain.directory.service;

import com.frazzle.main.domain.directory.dto.InviteOrCancelMemberRequestDto;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
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
public class CancelMemberInvitationTest {

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

    @Mock
    private Directory directory;

    private User user;
    private User member;
    private InviteOrCancelMemberRequestDto requestDto;

    @BeforeEach
    public void setUp(){
        user = User.createUser("1", "김싸피", "ssafy@ssafy.com", "kakao");
        member = User.createUser("2", "싸피", "ssafy@gmail.com", "kakao");
        requestDto = new InviteOrCancelMemberRequestDto(member.getUserId());
    }

    @Test
    @DisplayName("멤버 초대 취소 성공 테스트")
    public void 멤버_초대_취소_성공_테스트(){
        //given
        BDDMockito.given(userRepository.findByUserId(userPrincipal.getId())).willReturn(
                Optional.ofNullable(user));
        BDDMockito.given(directoryRepository.findByDirectoryId(directory.getDirectoryId())).willReturn(
                Optional.ofNullable(directory));
        BDDMockito.given(userRepository.findByUserId(member.getUserId())).willReturn(
                Optional.ofNullable(user)
        );
        BDDMockito.given(userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(
                BDDMockito.any(Directory.class),
                BDDMockito.any(User.class),
                BDDMockito.eq(true)
        )).willReturn(true);
        BDDMockito.given(userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(
                BDDMockito.any(Directory.class),
                BDDMockito.any(User.class),
                BDDMockito.eq(false)
        )).willReturn(true);

        //when
        Assertions.assertThatNoException().isThrownBy(
                ()->directoryService.cancelMemberInvitation(userPrincipal, requestDto, directory.getDirectoryId())
        );

        //then
        BDDMockito.then(userRepository)
                .should(times(2)).findByUserId(userPrincipal.getId());
        BDDMockito.then(directoryRepository)
                .should(times(1)).findByDirectoryId(directory.getDirectoryId());
        BDDMockito.then(userRepository)
                .should(times(2)).findByUserId(member.getUserId());
        BDDMockito.then(userDirectoryRepository).should(times(1)).existsByDirectoryAndUserAndIsAccept(
                BDDMockito.any(Directory.class),
                BDDMockito.any(User.class),
                BDDMockito.eq(true));
        BDDMockito.then(userDirectoryRepository).should(times(1)).existsByDirectoryAndUserAndIsAccept(
                BDDMockito.any(Directory.class),
                BDDMockito.any(User.class),
                BDDMockito.eq(true));
        BDDMockito.then(userDirectoryRepository).should(times(1)).deleteByUserAndDirectory(
                BDDMockito.any(User.class),
                BDDMockito.any(Directory.class)
        );
        BDDMockito.then(directory).should(times(1)).changePeopleNumber(-1);
    }

    @Test
    @DisplayName("멤버 초대 취소 실패 테스트")
    public void 멤버_초대_취소_실패_테스트() {
        BDDMockito.given(userRepository.findByUserId(userPrincipal.getId())).willReturn(
                Optional.ofNullable(user));
        BDDMockito.given(directoryRepository.findByDirectoryId(directory.getDirectoryId())).willReturn(
                Optional.ofNullable(directory));
        BDDMockito.given(userRepository.findByUserId(member.getUserId())).willReturn(
                Optional.ofNullable(user)
        );
        BDDMockito.given(userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(
                BDDMockito.any(Directory.class),
                BDDMockito.any(User.class),
                BDDMockito.eq(true)
        )).willReturn(true);
        BDDMockito.given(userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(
                BDDMockito.any(Directory.class),
                BDDMockito.any(User.class),
                BDDMockito.eq(false)
        )).willReturn(false);

        Assertions.assertThatThrownBy(() -> directoryService.cancelMemberInvitation(userPrincipal, requestDto, directory.getDirectoryId()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.DENIED_CANCEL_MEMBER.getMessage());
    }
}
