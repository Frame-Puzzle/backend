package com.frazzle.main.domain.board.service;

import com.frazzle.main.domain.board.dto.CreateBoardRequestDto;
import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.entity.BoardClearTypeFlag;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.global.models.UserPrincipal;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class BoardServiceTest {

    @Autowired
    private EntityManager entityManager;

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
    private List<Board> boardList = new ArrayList<>();
    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    public void setUp() {
        // 유저 생성
        testUser = User.createUser("1", "김싸피", "ssafy@ssafy.com", "kakao");
        //userRepo.save(testUser);
        userPrincipal = UserPrincipal.create(testUser);

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
        // 디렉토리 생성
        requestDto = new CreateDirectoryRequestDto("친구", "B208");
        directory = Directory.createDirectory(requestDto);
        directory.changePeopleNumber(5);
        directoryRepo.save(directory);

        requestDto = new CreateDirectoryRequestDto("가족", "Family");
        directory = Directory.createDirectory(requestDto);
        directory.changePeopleNumber(2);
        directoryRepo.save(directory);

        int puzzleSize = 30;
        CreateBoardRequestDto boardDto = new CreateBoardRequestDto(missions, keywords, puzzleSize);

        boardList.add(boardService.createBoard(userPrincipal, boardDto, directory.getDirectoryId()));

        missions = "#A#B#C";
        keywords = "#Dog#Cat#Cow";
        boardDto = new CreateBoardRequestDto(missions, keywords, 12);

        boardList.add(boardService.createBoard(userPrincipal, boardDto, directory.getDirectoryId()));

        missions = "#1#2#3";
        keywords = "#Eat#Fish#Travel";
        boardDto = new CreateBoardRequestDto(missions, keywords, 20);

        boardList.add(boardService.createBoard(userPrincipal, boardDto, directory.getDirectoryId()));
        // when
        //보드판 생성

        // then

        //Optional<Board> foundBoard = boardRepo.findById(board.getBoardId());
        //assertThat(foundBoard).isPresent();
        //assertThat(foundBoard.get()).isEqualTo(board);
    }

    @Test
    void 퍼즐판_조회(){
        퍼즐보드판_생성();
        //보드 id로 찾기
        Optional<Board> foundBoard = boardRepo.findById(boardList.get(0).getBoardId());
        //assertThat(foundBoard.isPresent()).isTrue();
        System.out.println(foundBoard.get());

        //디렉토리 id로 찾기
        List<Board> foundBoardList = boardService.findBoardsByDirectoryId(boardList.get(0).getDirectory().getDirectoryId());

        int a = 0;
    }

    @Test
    void 업데이트_테스트(){
        퍼즐보드판_생성();
        Board board = boardList.get(0);

        boardService.updateThumbnailUrl(board, "사진1"); //들어가면 안됨
        boardService.updateClearType(board, BoardClearTypeFlag.PUZZLE_GAME_CLEARED);
        boardService.updateThumbnailUrl(board, "사진2");

        boardService.changeIsVote(board);

        boardService.updateVoteCount(board,2);

        boardService.updatePieceCount(board, 10);

        boardRepo.save(board);
    }

    @Test
    void 보드_삭제_테스트()
    {
        업데이트_테스트();
        Board board = boardList.get(0);

        boardService.updateVoteCount(board,0);

        boardService.deleteBoard(board.getBoardId()); //삭제 실패

        boardService.updateVoteCount(board, 3);

        boardService.deleteBoard(board.getBoardId()); //삭제 완료
    }

}
