package com.frazzle.main.domain.piece.service;

import com.frazzle.main.domain.piece.dto.UpdatePieceRequestDto;
import com.frazzle.main.domain.piece.entity.Piece;
import com.frazzle.main.domain.piece.repository.PieceRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
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
    private final AwsService awsService;

    private User checkUser(UserPrincipal userPrincipal) {
        return userRepository.findByUserId(userPrincipal.getId())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_USER));
    }

    //퍼즐 조각을 조회할 때 진행하는 검증
    private void checkPiece(int directorId, Piece piece){
        if(piece.getBoard().getDirectory().getDirectoryId() != directorId){
            throw new CustomException(ErrorCode.NOT_DIRECTORY_MEMBER);
        }
    }

    //퍼즐 조각 상세 조회(piece id) (API)
    public Piece findPieceByPieceId(UserPrincipal userPrincipal, int directoryId, int pieceId){

        //유저검증
        checkUser(userPrincipal);

        //퍼즐 조각 탐색
        Piece piece = pieceRepository.findPieceByPieceId(pieceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_PIECE));

        //퍼즐 조각 검증
        checkPiece(directoryId, piece);

        return piece;
    }

    //퍼즐 조각 전체 조회(directory id) (API)
    public List<Piece> findPiecesByBoardId(UserPrincipal userPrincipal, int directoryId, int boardId){

        checkUser(userPrincipal);

        List<Piece> pieceList = pieceRepository.findAllByBoardBoardId(boardId);

        if(pieceList.isEmpty() || pieceList == null){
            throw new CustomException(ErrorCode.NOT_EXIST_PIECE);
        }

        //조각 검증
        for(Piece piece : pieceList){
            checkPiece(directoryId, piece);
        }

        return pieceList;
    }

    //퍼즐 조각 업로드
    @Transactional
    public void updatePiece(UserPrincipal userPrincipal, int directoryId, int pieceId, UpdatePieceRequestDto requestDto)
    {
        //1. 사용자 인증 및 인가 검증
        User user = checkUser(userPrincipal);

        Piece piece = pieceRepository.findPieceByPieceId(pieceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_PIECE));

        //1-1. 퍼즐 조각 검증
        checkPiece(directoryId, piece);


        //1-2. 퍼즐조각이 현재 수정 가능한지 검증 -> 등록이 되있다면 처음 등록한 유저만 수정이 가능
        User pieceUser = piece.getUser();

        //Feedback : Jpa에선 user 객체간 비교 가능?
        if(pieceUser != null && (user.getUserId() != pieceUser.getUserId())){
            throw new CustomException(ErrorCode.DENIED_UPDATE_PIECE);
        }

        //3. 파일 변환
        //TODO: multifile S3로 업로드 하고 url 받기
        MultipartFile imgFile = requestDto.getImgFile();
        String uuidUrl = awsService.uploadFile(imgFile, "");
        String url = awsService.getProfileUrl(uuidUrl);

        //3. 퍼즐 조각 수정
        piece.updatePieceDto(url, requestDto.getComment(), user);

        //4. Face Detection : 사람 수 파악
        int peopleCount = FindPeopleCountFromImg.inputImgUrl(piece.getImageUrl());

        piece.updatePeopleCount(peopleCount);
    }
}
