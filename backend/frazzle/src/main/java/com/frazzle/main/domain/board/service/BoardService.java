package com.frazzle.main.domain.board.service;

import com.frazzle.main.domain.board.dto.CreateBoardRequestDto;
import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardService {

    private final UserRepository userRepository;
    private final DirectoryRepository directoryRepository;
    private final BoardRepository boardRepository;

    public List<Board> findBoardsByDirectoryId(int directoryId)
    {
        //response
        return null;
    }

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
        //1. 유저 확인
        User user = userRepository.findByUserId(userPrincipal.getId());

        //디렉토리 탐색
        Directory directory = directoryRepository.findByDirectoryId(directoryID)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY));

        //보드 생성
        Board board = Board.createBoard(boardDto, user, directory);
        boardRepository.save(board);
        return board;
    }

    public void updateBoard(Board board)
    {

    }

    @Transactional
    public void deleteBoard(int boardId)
    {
        //TODO: 투표 조건 봐야됨
    }
}
