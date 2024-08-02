package com.frazzle.main.domain.board.service;

import com.frazzle.main.domain.board.dto.*;
import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.entity.BoardClearTypeFlag;
import com.frazzle.main.domain.board.entity.GlobalBoardSize;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.piece.dto.FindPieceResponseDto;
import com.frazzle.main.domain.piece.entity.Piece;
import com.frazzle.main.domain.piece.repository.PieceRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.global.aws.service.AwsService;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.global.utils.GenerateRandomNickname;
import com.frazzle.main.global.utils.ParseStringWord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private final AwsService awsService;

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

    public FindBoardAndPiecesResponseDto findImageAll(UserPrincipal userPrincipal, int directoryId, int boardId) {
        Board board = findBoardByBoardId(userPrincipal, boardId);
        Directory directory = directoryRepository.findByDirectoryId(directoryId).get();
        List<Piece> pieceList = pieceRepository.findAllByBoardBoardId(boardId);

        if(pieceList.isEmpty() || pieceList == null) {
            throw new CustomException(ErrorCode.NOT_EXIST_PIECE);
        }

        PieceListResponseDto[] pieceResponseDtoList = new PieceListResponseDto[pieceList.size()];

        int size = pieceList.size();
        int userId = userPrincipal.getId();

        for(int i = 0; i < size; i++){
            Piece p = pieceList.get(i);

            //해당 퍼즐 조각이 해당 사용자가 수정이 가능한지 체크한다.
            int authorityNumber = checkAuthority(userId, p);

            pieceResponseDtoList[i] = PieceListResponseDto.createPieceListResponseDto(
                    p.getPieceId(), authorityNumber, p.getPieceRow(), p.getPieceCol()
            );
        }

        String keywordToken[] = ParseStringWord.hashTagToStringToken(board.getKeyword());

        FindBoardAndPiecesResponseDto responseDto = FindBoardAndPiecesResponseDto.createFindBoardAndPiecesResponseDto(
                keywordToken,
                directory.getCategory(),
                directory.getDirectoryName(),
                ""+board.getBoardInNumber(),
                board.getBoardSize(),
                board.getUser().getNickname(),
                pieceResponseDtoList
                );

        return responseDto;
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

    //보드판 내의 썸네일 사진 등록, 1등 유저가 등록되고, 게임 클리어 시에만 가능
    @Transactional
    public void updateThumbnailUrl(UserPrincipal userPrincipal, int boardID, UpdateBoardThumbnailRequestDto requestDto) {

        Board board = findBoardByBoardId(userPrincipal, boardID);

        if((board.getClearType() == BoardClearTypeFlag.PUZZLE_GAME_CLEARED.getValue()
        && board.getUser() != null)){

            String uuid = awsService.uploadFile(requestDto.getThumbnailUrl(), "");
            String url = awsService.getProfileUrl(uuid);

            board.changeImageUrl(url);
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
    public void updateVoteCount(Board board, boolean isAccept) {
        if(isAccept){
            board.addVoteNumber();
        }
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

    public FindAllImageFromBoardResponseDto findAllPhoto(UserPrincipal userPrincipal, int boardId){

        checkUser(userPrincipal);

        List<Piece> pieceList = pieceRepository.findAllByBoardBoardId(boardId);

        FindPieceResponseDto[] pieceDtoList = new FindPieceResponseDto[pieceList.size()];

        for(int i = 0; i<pieceList.size(); i++) {
            pieceDtoList[i] = FindPieceResponseDto
                    .createPieceDto(
                            pieceList.get(i).getImageUrl(),
                            pieceList.get(i).getContent());
        }

        //보드 id를 통해 image 조회하기
        Optional<String> imgUrl = boardRepository.findThumbnailUrlByBoardId(boardId);

        FindAllImageFromBoardResponseDto responseDto = FindAllImageFromBoardResponseDto
                .createFindAllImageFromBoardResponseDto(imgUrl.get(), pieceDtoList);

        return responseDto;
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

        int row = GlobalBoardSize.minimumBoardRow;
        int col = GlobalBoardSize.minimumBoardColumn;

        if(boardSize < (row * col)){
            throw new CustomException(ErrorCode.NOT_EXIST_BOARD);
        }

        //BoardSize의 Row와 Col을 알아낸다.
        int level = GlobalBoardSize.maximumLevel;
        for(int i = 0; i< level; i++){
            if(boardSize == row * col){
                break;
            }
            row++;
            col++;
        }

        List<Piece> pieceList = new ArrayList<>();
        //퍼즐 조각을 생성한다. 0,0 부터 시작
        for(int i = 0; i< row; i++){
            for(int j = 0; j< col; j++){
                pieceList.add(Piece.createPiece(board, i, j));
            }
        }

        //#####TEST
        String guideMission = "Mission ";
        //#######

        //가이드 부여
        List<Integer> usingNumberList = getRandomNumber(boardSize, row);

        for(int i = 0; i< row; i++){
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
            throw new CustomException(ErrorCode.CANNOT_BE_NEGATIVE);
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

    //퍼즐판 전체조회 시 퍼즐 조각이 어떤 상태인지 확인하는 메소드
    private int checkAuthority(int userId, Piece piece){
        int result = 0;

        User pieceUser = piece.getUser();
        String imageUrl = piece.getImageUrl();
        String content = piece.getContent();

        //1. 전부 비어있는 상태
        if(pieceUser == null && imageUrl == null && content == null){
            result = Authority.EMPTY.getValue();
        } //2. 자신만 수정 가능한 상태
        else if(pieceUser.getUserId() == userId){
            result = Authority.UPDATE_ONLY_ME.getValue();
        } //3. 사진을 넣었던 유저가 사라진 상태
        else if(pieceUser == null && (imageUrl != null || content != null)){
            result = Authority.UPDATABLE.getValue();
        } //4. 다른 유저가 사진을 넣어서 수정할 수 없음
        else if(userId != pieceUser.getUserId() && imageUrl != null){
            result = Authority.CANNOT_UPDATE.getValue();
        }
        return result;
    }
}
