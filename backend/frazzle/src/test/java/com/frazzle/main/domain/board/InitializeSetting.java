package com.frazzle.main.domain.board;

import com.frazzle.main.domain.board.dto.CreateBoardRequestDto;
import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.board.service.BoardService;
import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.directory.service.DirectoryService;
import com.frazzle.main.domain.piece.repository.PieceRepository;
import com.frazzle.main.domain.piece.service.PieceService;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InitializeSetting {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected BoardService boardService;

    @Autowired
    protected PieceService pieceService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected DirectoryService directoryService;

    @Autowired
    protected DirectoryRepository directoryRepository;

    @Autowired
    protected UserDirectoryRepository userDirectoryRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected BoardRepository boardRepository;

    @Autowired
    protected PieceRepository pieceRepository;

    protected void init()
    {
        //##### 유저 생성 #####
        User user1 = User.createUser("1", "피카츄", "a@a.com", "kakao");
        User user2 = User.createUser("2", "파이리", "b@b.com", "google");
        User user3 = User.createUser("3", "꼬부기", "c@c.com", "kakao");
        User user4 = User.createUser("4", "피죤투", "d@d.com", "kakao");
        User user5 = User.createUser("5", "야도란", "e@e.com", "google");

        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
        userService.save(user4);
        userService.save(user5);

        //##### 디렉토리 생성 #####
        CreateDirectoryRequestDto requestDto1 = new CreateDirectoryRequestDto("친구", "베프");
        CreateDirectoryRequestDto requestDto2 = new CreateDirectoryRequestDto("가족", "팸팸");
        CreateDirectoryRequestDto requestDto3 = new CreateDirectoryRequestDto("반려동물", "뽀삐");
        CreateDirectoryRequestDto requestDto4 = new CreateDirectoryRequestDto("애인", "404 Not Found");

        Directory directory1 = Directory.createDirectory(requestDto1);
        Directory directory2 = Directory.createDirectory(requestDto2);
        Directory directory3 = Directory.createDirectory(requestDto3);
        Directory directory4 = Directory.createDirectory(requestDto4);

        directoryRepository.save(directory1);
        directoryRepository.save(directory2);
        directoryRepository.save(directory3);
        directoryRepository.save(directory4);

        //##### 유저 디렉토리 생성 #####

        //##### 보드판 생성 #####
        CreateBoardRequestDto brd1 = new CreateBoardRequestDto("#미션1#미션2#미션3", "#사과#바나나#포도", 12);
        CreateBoardRequestDto brd2 = new CreateBoardRequestDto("#안녕1#안녕2", "#가족", 20);
        CreateBoardRequestDto brd3 = new CreateBoardRequestDto("#A", "", 30);
        CreateBoardRequestDto brd4 = new CreateBoardRequestDto(null, null, 12);
        CreateBoardRequestDto brd5 = new CreateBoardRequestDto("#다#해#줬", "#잖#아", 20);
        CreateBoardRequestDto brd6 = new CreateBoardRequestDto("", "", 12);

        Board board1 = Board.createBoard(brd1);
        Board board2 = Board.createBoard(brd2);
        Board board3 = Board.createBoard(brd3);
        Board board4 = Board.createBoard(brd4);
        Board board5 = Board.createBoard(brd5);
        Board board6 = Board.createBoard(brd6);

        boardRepository.save(board1);
        boardRepository.save(board2);
        boardRepository.save(board3);
        boardRepository.save(board4);
        boardRepository.save(board5);
        boardRepository.save(board6);

    }


    protected void clearAll()
    {
        directoryRepository.deleteAll();
        userDirectoryRepository.deleteAll();
        userRepository.deleteAll();
        boardRepository.deleteAll();
        pieceRepository.deleteAll();
    }
}
