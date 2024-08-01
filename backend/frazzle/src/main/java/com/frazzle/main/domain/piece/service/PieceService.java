package com.frazzle.main.domain.piece.service;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.piece.dto.PieceDto;
import com.frazzle.main.domain.piece.entity.Piece;
import com.frazzle.main.domain.piece.repository.PieceRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PieceService {

    private final PieceRepository pieceRepository;
    private final UserRepository userRepository;
    private final UserDirectoryRepository userDirectoryRepository;
    private final BoardRepository boardRepository;
    private final DirectoryRepository directoryRepository;

    private User checkUser(UserPrincipal userPrincipal) {
        return userRepository.findByUserId(userPrincipal.getId())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_USER));
    }

    //퍼즐 조각을 조회할 때 진행하는 검증
    private void checkVerifyAccess(UserPrincipal userPrincipal, Piece piece){

        //1. 사용자 인증
        checkUser(userPrincipal);

        //2. 사용자 디렉토리 목록들을 조회함
        //userDirectoryRepository.find

        //3. 해당 퍼즐판이 소속된 보드를 조회함
        //4. 퍼즐 조각의 board에서 속한 디렉토리를 조회함
        int directoryId = piece.getBoard().getDirectory().getDirectoryId();





        //4. 사용자가 해당 디렉토리에 속한지 판단



    }

    public Piece findById(int pieceId){




        return null;
    }

    public List<Piece> findDirectoryByBoardId(){
        return null;
    }

    //퍼즐 조각 업로드
    @Transactional
    public void updatePiece(PieceDto pieceDto)
    {

    }

    //퍼즐 조각 전체 조회(directory id) (API)

    //퍼즐 조각 상세 조회(piece id) (API)

    //퍼즐 조각 사진 사람 수 확인
}
