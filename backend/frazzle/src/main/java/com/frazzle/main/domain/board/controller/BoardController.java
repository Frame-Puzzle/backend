package com.frazzle.main.domain.board.controller;

import com.frazzle.main.domain.board.dto.CreateBoardRequestDto;
import com.frazzle.main.domain.board.dto.UpdateVoteRequestDto;
import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.service.BoardService;
import com.frazzle.main.domain.piece.entity.Piece;
import com.frazzle.main.domain.piece.service.PieceService;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.global.utils.ResultDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/directories/{directoryID}/boards")
public class BoardController {

    private final BoardService boardService;
    private final PieceService pieceService;

    @PostMapping
    public ResponseEntity<ResultDto> createBoard(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateBoardRequestDto requestDto,
            @PathVariable("directoryID") int directoryID)
    {

        boardService.createBoard(userPrincipal, requestDto, directoryID);
        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(),
                +directoryID+"에 퍼즐판 생성 성공"));
    }

    //퍼즐판 및 퍼즐조각 전체 조회
    @GetMapping("/{boardID}")
    public ResponseEntity<ResultDto> findBoardAndPiece(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("directoryID") int directoryID,
            @PathVariable("boardID") int boardID)
    {
        Board board = boardService.findBoardByBoardId(userPrincipal, boardID);
        List<Piece> pieceList = pieceService.findPiecesByBoardId(userPrincipal, directoryID, boardID);

        return null;
    }

    //투표
    @PutMapping("/{boardID}/vote")
    public ResponseEntity<ResultDto> deleteVote(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            UpdateVoteRequestDto requestDto
            ) {
        return null;
    }

    //TODO: 퍼즐판 삭제 요청?

    //퍼즐판 내 전체 사진 조회
    @GetMapping("/{boardID}/images")
    public ResponseEntity<ResultDto> getBoardImages(){
        return null;
    }

    //썸네일 생성 및 수정
    @PutMapping("/{boardID}/thumbnails")
    public ResponseEntity<ResultDto> updateBoardThumbnails(){
        return null;
    }

}
