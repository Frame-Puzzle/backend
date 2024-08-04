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
import com.frazzle.main.domain.piece.service.PieceService;
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
    private final PieceService pieceService;

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

    public FindBoardAndPiecesResponseDto findBoardAndPieces(UserPrincipal userPrincipal, int boardId) {
        Board board = findBoardByBoardId(userPrincipal, boardId);
        Directory directory = directoryRepository.findByDirectoryId(board.getDirectory().getDirectoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY));

        List<Piece> pieceList = pieceService.findPiecesByBoardId(boardId);

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

        String thumbnailer = null;
        if(board.getUser() != null)
            thumbnailer = board.getUser().getNickname();

        FindBoardAndPiecesResponseDto responseDto = FindBoardAndPiecesResponseDto.createFindBoardAndPiecesResponseDto(
                keywordToken,
                directory.getCategory(),
                directory.getDirectoryName(),
                ""+board.getBoardInNumber(),
                board.getBoardSize(),
                thumbnailer,
                pieceResponseDtoList
                );

        return responseDto;
    }

    @Transactional
    public CreateBoardResponseDto createBoard(UserPrincipal userPrincipal,
                             CreateBoardRequestDto boardDto,
                             int directoryID) {
        //디렉토리 탐색
        Directory directory = directoryRepository.findByDirectoryId(directoryID)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY));

        //유저 확인
        User user = checkUser(userPrincipal);

        //디렉토리 유저 인증
        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        String[] keywordToken = boardDto.getKeyword();
        String mergedKeyword = ParseStringWord.StringToHashTag(keywordToken);

        //보드 생성
        Board board = Board.createBoard(boardDto, directory, mergedKeyword);

        //보드 제작 넘버 세팅
        countingBoard(board, directoryID);

        boardRepository.save(board);

        //퍼즐 조각들 생성
        String[] guideToken = boardDto.getGuide();
        List<Piece> pieceList = createPiece(board, guideToken);

        for(Piece p : pieceList){
            pieceService.savePiece(p);
        }

        return CreateBoardResponseDto.builder().boardId(board.getBoardId()).build();
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

        //게임을 클리어했는지 판단, 유저가 등록되어있는지 판단.
        if((board.getClearType() == BoardClearTypeFlag.PUZZLE_GAME_CLEARED.getValue()) && board.getUser() != null){

            //기존 이미지가 존재하면 삭제한다.
            String imageUrl = board.getThumbnailUrl();

            if(imageUrl != null){
                awsService.deleteImage(imageUrl);
            }

            imageUrl = awsService.uploadFile(requestDto.getThumbnailUrl());

            board.changeImageUrl(imageUrl);
        }
    }

    //클리어 타입 변경
    @Transactional
    public void updateClearType(Board board, BoardClearTypeFlag flag) {
        board.changeClearType(flag);
    }

    //투표 여부 변경
    @Transactional
    public void changeIsVote(Board board) {
        board.changeVote();
    }

    @Transactional
    public boolean updateVoteCount(UserPrincipal userPrincipal, int boardId, boolean isAccept) {
        Board board = findBoardByBoardId(userPrincipal, boardId);
        board.enableVote(true);

        if(isAccept){
            board.addVoteNumber();
        }

        //삭제 판단 TODO: 로직 개선하기
        if(board.getVoteNumber() > board.getDirectory().getPeopleNumber()){
            deleteBoard(boardId);
            return true;
        }

        return false;
    }

    @Transactional
    public void deleteBoard(int boardId){
        List<Piece> pieceList = pieceService.findPiecesByBoardId(boardId);

        for(Piece p : pieceList){
            pieceService.deletePiece(p.getPieceId());
        }

        boardRepository.deleteById(boardId);
    }

    @Transactional
    public void updatePieceCount(Board board, int pieceCount) {
        board.changePieceCount(pieceCount);
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

        List<Piece> pieceList = pieceService.findPiecesByBoardId(boardId);

        FindPieceResponseDto[] pieceDtoList = new FindPieceResponseDto[pieceList.size()];

        for(int i = 0; i<pieceList.size(); i++) {
            pieceDtoList[i] = FindPieceResponseDto
                    .createPieceDto(
                            pieceList.get(i).getImageUrl(),
                            pieceList.get(i).getContent());
        }
        //보드 id를 통해 image 조회하기
        String imgUrl = boardRepository.findThumbnailUrlByBoardId(boardId)
                .orElseThrow(()-> new CustomException(ErrorCode.IMAGE_NOT_FOUND));

        FindAllImageFromBoardResponseDto responseDto = FindAllImageFromBoardResponseDto
                .createFindAllImageFromBoardResponseDto(imgUrl, pieceDtoList);

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
    private List<Piece> createPiece(Board board, String[] guides){
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

        //가이드 미션 부여
        String[] guideMission = guides;
        if(guideMission != null){
            int guideCount = guideMission.length;

            List<Integer> usingNumberList = getRandomNumber(boardSize, guideCount);

            for(int i = 0; i< guideCount; i++){
                pieceList.get(usingNumberList.get(i))
                        .updateMission(guideMission[i]);
            }
        }

        return pieceList;
    }

    //board 넓이만큼의 수 중에서 guideCount개의 미션을 집어넣는다.
    private List<Integer> getRandomNumber(int boardSize, int guideCount){
        if(boardSize < 0) {
            throw new CustomException(ErrorCode.CANNOT_BE_NEGATIVE);
        }
        List<Integer> numberList = new ArrayList<>();

        for(int i = 0; i< guideCount; i++){
            int result = GenerateRandomNickname.getRandom().nextInt(boardSize);

            //중복되는 위치일 시 위치를 다시 부여한다.
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

