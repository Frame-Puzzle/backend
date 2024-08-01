package com.frazzle.main.domain.board.service;

import com.frazzle.main.domain.board.dto.CreateBoardRequestDto;
import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.entity.BoardClearTypeFlag;
import com.frazzle.main.domain.board.entity.GlobalBoardSize;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.piece.entity.Piece;
import com.frazzle.main.domain.piece.repository.PieceRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.global.utils.GenerateRandomNickname;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardService {

    private final UserRepository userRepository;
    private final DirectoryRepository directoryRepository;
    private final UserDirectoryRepository userDirectoryRepository;
    private final BoardRepository boardRepository;

    private final PieceRepository pieceRepository;

    private User checkUser(UserPrincipal userPrincipal) {
        return userRepository.findByUserId(userPrincipal.getId())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_USER));
    }

    public Board findBoardByBoardId(UserPrincipal userPrincipal, int boardId) {
        checkUser(userPrincipal);

        Board board = boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_BOARD));

        return board;
    }

    public List<Board> findBoardsByDirectoryId(int directoryId) {
        return boardRepository.findByDirectoryDirectoryId(directoryId);
    }

    @Transactional
    public Board createBoard(UserPrincipal userPrincipal,
                             CreateBoardRequestDto boardDto,
                             int directoryID) {
        //디렉토리 탐색
        Directory directory = directoryRepository.findByDirectoryId(directoryID)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY));

        //Optional<Directory> directory = directoryRepository.findByDirectoryId(directoryID);

        //유저 확인
        User user = checkUser(userPrincipal);

        //디렉토리 유저 인증
        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        //보드 생성
        Board board = Board.createBoard(boardDto);
        board.updateDirectory(directory);

        //보드 제작 넘버 세팅
        countingBoard(board, directoryID);

        /*
        TODO: 미션 생성
         */

        //퍼즐 조각들 생성
        List<Piece> pieceList = createPiece(board);

        boardRepository.save(board);
        return board;
    }

    //썸네일 유저 등록, 테스트 필요
    @Transactional
    public void updateUserFromBoard(int boardId, UserPrincipal userPrincipal)
    {
        User user = checkUser(userPrincipal);
        Board board = findBoardByBoardId(userPrincipal, boardId);

        board.updateUser(user);
    }

    //썸네일 사진 등록, 1등 유저가 등록되고, 게임 클리어 시에만 가능
    @Transactional
    public void updateThumbnailUrl(Board board, String thumbnailUrl) {
        if((board.getClearType() == BoardClearTypeFlag.PUZZLE_GAME_CLEARED.getValue()
        && board.getUser() != null)){
            board.changeImageUrl(thumbnailUrl);
        }
    }

    //클리어 타입 변경
    @Transactional
    public void updateClearType(Board board, BoardClearTypeFlag flag)
    {
        board.changeClearType(flag);
    }

    //투표 여부 변경
    @Transactional
    public void changeIsVote(Board board) {
        board.changeVote();
    }

    @Transactional
    public void updateVoteCount(Board board, int voteCount) {
        board.changeVoteNumber(voteCount);
    }

    @Transactional
    public void updatePieceCount(Board board, int pieceCount) {
        board.changePieceCount(pieceCount);
    }

    @Transactional
    public void deleteBoard(UserPrincipal userPrincipal, int boardId) {
        Board board = findBoardByBoardId(userPrincipal, boardId);

        Directory directory = board.getDirectory();

        if(directory == null)
            throw new CustomException(ErrorCode.NOT_EXIST_DIRECTORY);

        //디렉토리 내에 존재하는 총 인원 수 현재 와 비교한다.
        //int maxPeople = directory.getPeopleNumber();

        //int voteNumber = board.getVoteNumber();

        //과반수 체크
        //if(!checkDeleteCondition(maxPeople, voteNumber)){return false;}
        
        boardRepository.delete(board);
        //return true;
    }

    //넘버 수 : 해당 디렉토리 소속의 보드판이 몇 개인지 확인한다.
    @Transactional
    public void countingBoard(Board board, int directoryId)
    {
        List<Board> boardList = findBoardsByDirectoryId(directoryId);
        int result = (boardList.isEmpty() || boardList == null) ? 1 : boardList.size()+1;
        board.changeBoardInNumber(result);
    }

    public List<Piece> findAllPhoto(int boardId){
        return pieceRepository.findAllByBoardBoardId(boardId);
    }

    //### 내장 함수

    //과반수 체크 메소드
    private boolean checkDeleteCondition(int maxPeople, int voteNum){
        if(maxPeople == 0)
            return false;

        return voteNum > Math.ceil(maxPeople / 2.0);
    }

    //퍼즐 조각 생성
    private List<Piece> createPiece(Board board){
        int boardSize = board.getBoardSize();

        int maxRow = GlobalBoardSize.minimumBoardRow;
        int maxCol = GlobalBoardSize.maximumBoardColumn;

        if(boardSize < (maxRow * maxCol)){
            throw new CustomException(ErrorCode.NOT_EXIST_BOARD);
        }

        //BoardSize의 Row와 Col을 알아낸다.
        int level = GlobalBoardSize.maximumLevel;
        for(int i = 0; i< level; i++){
            if(boardSize == maxRow * maxCol){
                break;
            }
            maxRow++;
            maxCol++;
        }

        List<Piece> pieceList = new ArrayList<>();
        //퍼즐 조각을 생성한다. 0,0 부터 시작
        for(int i = 0; i< maxRow; i++){
            for(int j = 0; j< maxCol; j++){
                pieceList.add(Piece.createPiece(board, i, j));
            }
        }

        /*
        TODO: 퍼즐 조각들 생성
        난수로 특정 조각에 가이드미션 집어넣기
         */
        //#####TEST
        String guideMission = "Mission ";
        //#######

        //가이드 부여
        List<Integer> usingNumberList = getRandomNumber(boardSize, maxRow);

        for(int i = 0; i< maxRow; i++){
            pieceList.get(usingNumberList.get(i))
                    .updateMission(guideMission + (i + 1));
        }

        for(Piece p : pieceList){
            pieceRepository.save(p);
        }

        return pieceList;
    }

    private List<Integer> getRandomNumber(int maxNumber, int count){
        if(maxNumber < 0) {
            throw new IllegalArgumentException("number cannot be negative");
        }
        List<Integer> numberList = new ArrayList<>();

        for(int i = 0; i< count; i++){
            int result = GenerateRandomNickname.getRandom().nextInt(maxNumber);

            for(int n : numberList) {
                if(result == n) {
                    i--;
                    break;
                }
            }
            numberList.add(result);
        }
        return numberList;
    }
}
