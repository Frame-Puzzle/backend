package com.frazzle.main.domain.board;

import com.frazzle.main.domain.board.dto.CreateBoardRequestDto;
import com.frazzle.main.domain.board.dto.CreateBoardResponseDto;
import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.board.service.BoardService;
import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.directory.service.DirectoryService;
import com.frazzle.main.domain.piece.entity.Piece;
import com.frazzle.main.domain.piece.repository.PieceRepository;
import com.frazzle.main.domain.piece.service.PieceService;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.global.models.UserPrincipal;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class InitializeSetting {

    //내 계정임
    protected final int myUserId = 3;
    protected UserPrincipal myUserPrincipal;
    protected User myUser;
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

    //#########################################
    protected List<Integer> userIdList;
    protected List<Integer> directoryIdList;
    protected List<Integer> userDirectoryIdList;
    protected List<Integer> boardIdList;
    protected List<Integer> pieceIdList;

    protected  void myUserSetting() {
        myUserPrincipal = UserPrincipal.create(userService.findUser(myUserId));
        myUser = userService.findUser(myUserId);
    }

    protected void init()
    {
        userIdList = new ArrayList<>();
        directoryIdList = new ArrayList<>();
        userDirectoryIdList = new ArrayList<>();
        boardIdList = new ArrayList<>();
        pieceIdList = new ArrayList<>();

        //##### 유저 생성 #####
        User user1 = User.createUser("1", "피카츄", "a@a.com", "kakao");
        User user2 = User.createUser("2", "파이리", "b@b.com", "google");
        User user3 = User.createUser("3", "꼬부기", "c@c.com", "kakao");
        User user4 = User.createUser("4", "피죤투", "d@d.com", "kakao");
        User user5 = User.createUser("5", "야도란", "e@e.com", "google");

        userIdList.add(user1.getUserId());
        userIdList.add(user2.getUserId());
        userIdList.add(user3.getUserId());
        userIdList.add(user4.getUserId());
        userIdList.add(user5.getUserId());

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

        directoryIdList.add(directory1.getDirectoryId());
        directoryIdList.add(directory2.getDirectoryId());
        directoryIdList.add(directory3.getDirectoryId());
        directoryIdList.add(directory4.getDirectoryId());

        //##### 유저 디렉토리 생성 #####

        UserDirectory ud1 = UserDirectory.createUserDirectory(directory1, myUser, true);
        UserDirectory ud2 = UserDirectory.createUserDirectory(directory2, myUser, true);
        UserDirectory ud3 = UserDirectory.createUserDirectory(directory3, myUser, true);
        UserDirectory ud4 = UserDirectory.createUserDirectory(directory4, myUser, true);

        userDirectoryIdList.add(ud1.getUserDirectoryId());
        userDirectoryIdList.add(ud2.getUserDirectoryId());
        userDirectoryIdList.add(ud3.getUserDirectoryId());
        userDirectoryIdList.add(ud4.getUserDirectoryId());

        userDirectoryRepository.save(ud1);
        userDirectoryRepository.save(ud2);
        userDirectoryRepository.save(ud3);
        userDirectoryRepository.save(ud4);

        //##### 보드판 생성 #####
        String[] guide = {"미션1", "미션2", "미션3"};
        String[] keyword = {"사과", "바나나", "포도"};
        CreateBoardRequestDto brd1 = new CreateBoardRequestDto(guide, keyword, 12);

        String[] guide2 = {"피카츄", "라이츄","파이리","꼬부기"};
        String[] keyword2 = {"가족"};
        CreateBoardRequestDto brd2 = new CreateBoardRequestDto(guide2, keyword2, 20);

        String[] guide3 = {"A", "B", "C", "D", "E"};
        String[] keyword3 = {""};
        CreateBoardRequestDto brd3 = new CreateBoardRequestDto(guide3, keyword3, 30);

        CreateBoardRequestDto brd4 = new CreateBoardRequestDto(null, null, 12);

        String[] guide5 = {"다","해","줬","어"};
        String[] keyword5 = {"정","상","화"};
        CreateBoardRequestDto brd5 = new CreateBoardRequestDto(guide5, keyword5, 20);

        String[] guide6 = {""};
        String[] keyword6 = {""};
        CreateBoardRequestDto brd6 = new CreateBoardRequestDto(guide6, keyword6, 12);

        CreateBoardResponseDto board1 = boardService.createBoard(myUserPrincipal, brd1, directoryIdList.get(0));
        CreateBoardResponseDto board2 = boardService.createBoard(myUserPrincipal, brd2, directoryIdList.get(1));
        CreateBoardResponseDto board3 = boardService.createBoard(myUserPrincipal, brd3, directoryIdList.get(2));
        CreateBoardResponseDto board4 = boardService.createBoard(myUserPrincipal, brd4, directoryIdList.get(3));
        CreateBoardResponseDto board5 = boardService.createBoard(myUserPrincipal, brd5, directoryIdList.get(1));
        CreateBoardResponseDto board6 = boardService.createBoard(myUserPrincipal, brd6, directoryIdList.get(3));

        boardIdList.add(board1.getBoardId());
        boardIdList.add(board2.getBoardId());
        boardIdList.add(board3.getBoardId());
        boardIdList.add(board4.getBoardId());
        boardIdList.add(board5.getBoardId());
        boardIdList.add(board6.getBoardId());

        List<Piece> pieceList;

        pieceList = pieceService.findPiecesByBoardId(board1.getBoardId());

        addPiece(pieceList);

       pieceList = pieceService.findPiecesByBoardId(board2.getBoardId());

        addPiece(pieceList);

        pieceList = pieceService.findPiecesByBoardId(board3.getBoardId());

        addPiece(pieceList);

        pieceList = pieceService.findPiecesByBoardId(board4.getBoardId());

        addPiece(pieceList);

        pieceList = pieceService.findPiecesByBoardId(board5.getBoardId());

        addPiece(pieceList);

        pieceList = pieceService.findPiecesByBoardId(board6.getBoardId());

        addPiece(pieceList);
    }

    protected void clearAll()
    {
//        for(int i : pieceIdList) {
//            pieceRepository.deleteById(i);
//        }
//
//        for(int i : boardIdList) {
//            boardRepository.deleteById(i);
//        }
//        for(int i : userIdList) {
//            userRepository.deleteById(i);
//        }
//        for(int i : userDirectoryIdList) {
//            userDirectoryRepository.deleteById(i);
//        }
//
//        for(int i : directoryIdList) {
//            directoryRepository.deleteById(i);
//        }


    }

    private void addPiece(List<Piece> pieceList)
    {
        for (Piece p : pieceList) {
            pieceIdList.add(p.getPieceId());
        }
    }
}
