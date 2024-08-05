package com.frazzle.main.domain.piece.service;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.entity.BoardClearTypeFlag;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.piece.dto.FindPieceResponseDto;
import com.frazzle.main.domain.piece.dto.UpdatePieceRequestDto;
import com.frazzle.main.domain.piece.entity.Piece;
import com.frazzle.main.domain.piece.repository.PieceRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.global.aws.service.AwsService;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.global.utils.FindPeopleCountFromImg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PieceService {

    private final PieceRepository pieceRepository;
    private final UserRepository userRepository;
    private final DirectoryRepository directoryRepository;
    private final UserDirectoryRepository userDirectoryRepository;
    private final AwsService awsService;
    private final FindPeopleCountFromImg findPeopleCountFromImg;

    private Directory checkDirectory(int directoryId) {
        return directoryRepository.findByDirectoryId(directoryId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY));
    }

    //퍼즐 조각을 조회할 때 진행하는 검증
    private void checkUserAndDirectory(User user, Directory directory){
        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user,true)) {
            throw new CustomException(ErrorCode.NOT_DIRECTORY_MEMBER);
        }
    }

    //퍼즐 조각 상세 조회(piece id) (API)
    public FindPieceResponseDto findPieceByPieceId(UserPrincipal userPrincipal, int pieceId){
        //유저 조회
        User user = userPrincipal.getUser();

        //퍼즐 조각 탐색
        Piece piece = pieceRepository.findPieceByPieceId(pieceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_PIECE));

        //유저검증
        checkUserAndDirectory(user, checkDirectory(piece.getBoard().getDirectory().getDirectoryId()));

        return FindPieceResponseDto.createPieceDto(piece.getImageUrl(), piece.getContent());
    }

    //퍼즐 조각 전체 조회
    public List<Piece> findPiecesByBoardId(int boardId){
        List<Piece> pieceList = pieceRepository.findAllByBoardBoardId(boardId);

        if(pieceList == null || pieceList.isEmpty()){
            throw new CustomException(ErrorCode.NOT_EXIST_PIECE);
        }

        return pieceList;
    }

    //퍼즐 조각 업로드
    @Transactional
    public boolean updatePiece(UserPrincipal userPrincipal, int pieceId, MultipartFile imgFile, String comment)
    {
        //퍼즐조각이 처음으로 수정되는 경우
        boolean isFirstUpdate = true;

        //0. 퍼즐 조각 조회,
        //Directory id를 알아내야 해서 인증 이전에 조회한다.
        Piece piece = pieceRepository.findPieceByPieceId(pieceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_PIECE));

        //1. 사용자 인증 및 인가 검증
        User user = userPrincipal.getUser();
        checkUserAndDirectory(user, checkDirectory(piece.getBoard().getDirectory().getDirectoryId()));

        //1-1. 퍼즐조각이 현재 수정 가능한지 검증 -> 등록이 되있다면 처음 등록한 유저만 수정이 가능
        User pieceUser = piece.getUser();
        
        if(pieceUser != null && (user.getUserId() != pieceUser.getUserId())){
            throw new CustomException(ErrorCode.DENIED_UPDATE_PIECE);
        }

        //퍼즐조각이 등록된 적이 있었는지 여부
        if(piece.getImageUrl() != null) {
            isFirstUpdate = false;
        }

        //2. 파일 변환 multifile S3로 업로드 하고 url 받기
//        MultipartFile imgFile = requestDto.getImgFile();

        //3. Face Detection : 사람 수 파악
        int peopleCount = findPeopleCountFromImg.analyzeImageFile(imgFile);

        //4. 퍼즐 조각 이미지 업로드 전 삭제
        String imageUrl = piece.getImageUrl();
        if(imageUrl != null) {
            awsService.deleteImage(imageUrl);
        }

        //사진 업로드 후 고유url 반환
        imageUrl = awsService.uploadFile(imgFile);

        //url을 통해 S3에서 이미지 가져오기
        String url = awsService.getImageUrl(imageUrl);

        //5. 퍼즐 조각 수정
        piece.updatePieceDto(url, comment, user);

        //6. 퍼즐판 완성 체크
        Board board = piece.getBoard();

        //첫 등록 시 pieceCount를 올려준다.
        if(isFirstUpdate) {
            board.addPieceCount();
        }

        if(board.getPieceCount() == board.getBoardSize()){
            board.changeClearType(BoardClearTypeFlag.PUZZLE_CLEARED);
            return true;
        }

        return false;
    }

    @Transactional
    public void savePiece(Piece piece){
        pieceRepository.save(piece);
    }

    @Transactional
    public void deletePiece(int pieceId){
        pieceRepository.deleteById(pieceId);
    }
}
