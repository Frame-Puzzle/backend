package com.frazzle.main.domain.board.service;

import com.frazzle.main.domain.board.dto.CreateBoardRequestDto;
import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.user.repository.UserRepositoryImpl;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.global.models.UserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    private BoardRepository boardRepo;

    private UserRepository userRepo;

    private DirectoryRepository directoryRepo;

    private UserPrincipal userPrincipal;

    private CreateDirectoryRequestDto requestDto;
    private User testUser;
    private int userId = 2;
    private Directory directory;
    private UserDirectory userDirectory;
    private String missions;
    private String keywords;

    @BeforeEach
    public void setUp()
    {

        //유저 생성
        testUser = User.createUser("2","김싸피", "ssafy@ssafy.com", "kakao");
        userPrincipal = UserPrincipal.create(testUser);
        userRepo.save(testUser);

        //디렉토리 생성
        requestDto = new CreateDirectoryRequestDto(
                "친구", "B208"
        );
        directory = Directory.createDirectory(requestDto);
        directoryRepo.save(directory);

        userDirectory = UserDirectory.createUserDirectory(directory, testUser, true);

        missions = "#성심당에서 사진 찍기#단체로 볼하트 만들기#호두아몬드율무차 들고 촬영";
        keywords = "#대전#호두#여행";

        boardService = new BoardService(userRepo, directoryRepo, boardRepo);
    }

    @AfterEach
    public void afterEach()
    {
        userRepo.deleteAll();
        directoryRepo.deleteAll();
        boardRepo.deleteAll();
    }

    @Test
    void 퍼즐보드판_생성() {

        //given
        //BDDMockito.given(userPrincipal.getId()).willReturn(userId);
        //BDDMockito.given(userRepo.findByUserId(userId)).willReturn(testUser);
        //BDDMockito.given(directoryRepo.save(BDDMockito.any(Directory.class))).willReturn(directory);
//        directoryRepo.save(directory);

        //3x4 4x5 5x6
        int puzzleSize = 30;
        CreateBoardRequestDto boardDto = new CreateBoardRequestDto(missions, keywords, puzzleSize);

        //when
        Board board = boardService.createBoard(userPrincipal, boardDto, directory.getDirectoryId());
        assertThat(board).isEqualTo(directory);

        //then
        boardRepo.save(board);
    }
}