package com.frazzle.main.domain.piece.service;

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

    private User checkUser(UserPrincipal userPrincipal) {
        return userRepository.findByUserId(userPrincipal.getId())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_USER));
    }

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

        //퍼즐 조각 탐색
        Piece piece = pieceRepository.findPieceByPieceId(pieceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_PIECE));

        //유저검증
        checkUserAndDirectory(checkUser(userPrincipal),
                checkDirectory(piece.getBoard().getDirectory().getDirectoryId()));

        return FindPieceResponseDto.createPieceDto(piece.getImageUrl(), piece.getContent());
    }

    //퍼즐 조각 전체 조회(directory id) (API)
    public List<Piece> findPiecesByBoardId(UserPrincipal userPrincipal, int directoryId, int boardId){

        checkUserAndDirectory(checkUser(userPrincipal), checkDirectory(directoryId));

        List<Piece> pieceList = pieceRepository.findAllByBoardBoardId(boardId);

        if(pieceList.isEmpty() || pieceList == null){
            throw new CustomException(ErrorCode.NOT_EXIST_PIECE);
        }

        return pieceList;
    }

    //퍼즐 조각 업로드
    @Transactional
    public void updatePiece(UserPrincipal userPrincipal, int pieceId, String comment, MultipartFile profileImg)
    {
        //0. 퍼즐 조각 조회,
        //Directory id를 알아내야 해서 인증 이전에 조회한다.
        Piece piece = pieceRepository.findPieceByPieceId(pieceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_PIECE));

        //1. 사용자 인증 및 인가 검증
        User user = checkUser(userPrincipal);
        checkUserAndDirectory(user, checkDirectory(piece.getBoard().getDirectory().getDirectoryId()));

        //1-1. 퍼즐조각이 현재 수정 가능한지 검증 -> 등록이 되있다면 처음 등록한 유저만 수정이 가능
        User pieceUser = piece.getUser();

        //Feedback : Jpa에선 user 객체간 비교 가능?
        if(pieceUser != null && (user.getUserId() != pieceUser.getUserId())){
            throw new CustomException(ErrorCode.DENIED_UPDATE_PIECE);
        }

        //2. 파일 변환 multifile S3로 업로드 하고 url 받기
//        MultipartFile imgFile = requestDto.getImgFile();

        //3. Face Detection : 사람 수 파악
        int peopleCount = findPeopleCountFromImg.analyzeImageFile(profileImg);

        String url = awsService.uploadFile(profileImg);

        //4. 퍼즐 조각 수정
        piece.updatePieceDto(url, comment, user);

        piece.updatePeopleCount(peopleCount);
    }
}
