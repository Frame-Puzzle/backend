package com.frazzle.main.domain.directory.service;

import com.frazzle.main.domain.board.dto.CreateBoardRequestDto;
import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.directory.dto.BoardListDto;
import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.domain.directory.dto.DetailDirectoryResponsetDto;
import com.frazzle.main.domain.directory.dto.MemberListDto;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DetailDirectoryServiceTest {

    @InjectMocks
    DirectoryService directoryService;

    @Mock
    DirectoryRepository directoryRepository;

    @Mock
    BoardRepository boardRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserDirectoryRepository userDirectoryRepository;

    @Mock
    UserPrincipal userPrincipal;

    private Directory directory;
    private List<User> memberList;
    private List<Board> boardList;
    private DetailDirectoryResponsetDto responsetDto;

    @BeforeEach
    void setUp() {
        directory = Directory.createDirectory(new CreateDirectoryRequestDto("친구", "싸피"));
        memberList = new ArrayList<>();
        memberList.add(User.createUser("1", "김싸피", "ssafy@ssafy.com", "kakao"));
        boardList = new ArrayList<>();
        boardList.add(Board.createBoard( new CreateBoardRequestDto(new String[]{"d"}, new String[]{"d"}, 12), directory, "d"));

        List<MemberListDto> memberListDtos = new ArrayList<>();
        memberListDtos.add(MemberListDto.createMemberList(memberList.get(0)));

        List<BoardListDto> boardListDtos = new ArrayList<>();
        boardListDtos.add(BoardListDto.createBoardList(boardList.get(0)));

        responsetDto = DetailDirectoryResponsetDto.createDetailDirectoryRequestDto(directory, true, memberListDtos, boardListDtos);
    }

    @Test
    @DisplayName("디렉토리 상세 조회 성공 테스트")
    public void 디렉토리_상세_조회_성공_테스트(){
        //given
        BDDMockito.given(userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(
                directory, userPrincipal.getUser(), true
        )).willReturn(true);
        BDDMockito.given(directoryRepository.findByDirectoryId(directory.getDirectoryId())).willReturn(Optional.ofNullable(directory));
        BDDMockito.given(userRepository.findDirectoryUsers(directory)).willReturn(memberList);
        BDDMockito.given(boardRepository.findBoards(directory.getDirectoryId())).willReturn(boardList);

        //when
        DetailDirectoryResponsetDto response = directoryService.findDetailDirectory(userPrincipal, directory.getDirectoryId());

    }
}
