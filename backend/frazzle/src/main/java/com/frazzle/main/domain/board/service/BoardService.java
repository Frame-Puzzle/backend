package com.frazzle.main.domain.board.service;

import com.frazzle.main.domain.board.dto.CreateBoardRequestDto;
import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.entity.BoardClearTypeFlag;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
    public List<Board> findBoardsByDirectoryId(int directoryId)
    {
        //response
        return null;
    }

    @Transactional
    public Board findBoardByBoardId(int boardId)
    {
        //response
        return null;
    }

    @Transactional
    public Board createBoard(UserPrincipal userPrincipal,
                             CreateBoardRequestDto boardDto,
                             int directoryID)
    {
        //유저 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );;

        //디렉토리 탐색
        Directory directory = directoryRepository.findByDirectoryId(directoryID)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY));

        //디렉토리 유저 인증
        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)) {
            throw new CustomException(ErrorCode.DENIED_UPDATE);
        }

        //보드 생성
        Board board = Board.createBoard(boardDto, directory);
        boardRepository.save(board);
        return board;
    }

    //썸네일 유저 등록
    @Transactional
    public void updateUserFromBoard(UserPrincipal userPrincipal)
    {

    }

    //썸네일 사진 등록
    @Transactional
    public void updateThumbnailUrl(String thumbnailUrl)
    {

    }

    //클리어 타입 변경
    @Transactional
    public void updateClearType(Board board, BoardClearTypeFlag flag)
    {
        board.changeClearType(flag);
    }

    //투표 여부 변경
    @Transactional
    public void changeIsVote(Board board)
    {
        board.changeVote();
    }

    @Transactional
    public void updateVoteCount(Board board, int voteCount)
    {
        board.changeVoteNumber(voteCount);
    }

    @Transactional
    public void updatePieceCount(Board board, int pieceCount)
    {
        board.changePieceCount(pieceCount);
    }

    @Transactional
    public void deleteBoard(int boardId)
    {
        //TODO: 투표 조건 봐야됨
    }
}
