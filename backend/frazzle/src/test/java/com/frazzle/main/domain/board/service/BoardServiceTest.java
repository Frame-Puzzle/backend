package com.frazzle.main.domain.board.service;

import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.global.models.UserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class BoardServiceTest {

    //@Autowired
    //private EntityManager entityManager;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private DirectoryRepository directoryRepo;

    private UserPrincipal userPrincipal;
    private CreateDirectoryRequestDto requestDto;
    private User testUser;
    private Directory directory;
    private String missions;
    private String keywords;

    @BeforeEach
    public void setUp() {
        // 유저 생성
        testUser = User.createUser("1", "김싸피", "ssafy@ssafy.com", "kakao");
        //userRepo.save(testUser);
        userPrincipal = UserPrincipal.create(testUser);

        // 디렉토리 생성
        requestDto = new CreateDirectoryRequestDto("친구", "B208");
        directory = Directory.createDirectory(requestDto);
        directory.changePeopleNumber(5);

        // 유저 디렉토리 생성
        //UserDirectory.createUserDirectory(directory, testUser, true);

        missions = "#성심당에서 사진 찍기#단체로 볼하트 만들기#호두아몬드율무차 들고 촬영";
        keywords = "#대전#호두#여행";
    }

    @AfterEach
    public void afterEach() {
        boardRepo.deleteAll();
        userRepo.deleteAll();
        directoryRepo.deleteAll();
    }

    @Test
    void 퍼즐보드판_생성() {
        // given
//        int puzzleSize = 30;
//        CreateBoardRequestDto boardDto = new CreateBoardRequestDto(missions, keywords, puzzleSize);
//
//        //directoryRepo.save(directory);
//
//        // when
//        //보드판 생성
//        Board board = Board.createBoard(boardDto, directory);
//
//        // then
//        boardRepo.save(board);
//        Optional<Board> foundBoard = boardRepo.findById(board.getBoardId());
//        assertThat(foundBoard).isPresent();
//        assertThat(foundBoard.get()).isEqualTo(board);
    }


}
