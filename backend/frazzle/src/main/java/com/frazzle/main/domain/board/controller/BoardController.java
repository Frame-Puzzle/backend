package com.frazzle.main.domain.board.controller;

import com.frazzle.main.domain.board.dto.*;
import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.service.BoardService;
import com.frazzle.main.domain.piece.dto.FindPieceResponseDto;
import com.frazzle.main.domain.piece.dto.UpdatePieceRequestDto;
import com.frazzle.main.domain.piece.entity.Piece;
import com.frazzle.main.domain.piece.service.PieceService;
import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.global.utils.ResultDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
            @PathVariable("directoryID") int directoryID,
            @Valid @RequestBody CreateBoardRequestDto requestDto)
    {

        boardService.createBoard(userPrincipal, requestDto, directoryID);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                "퍼즐판 생성 성공"));
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

        //TODO: responseDto 완성하기
        FindBoardAndPiecesResponseDto responseDto;

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "퍼즐판 생성 성공"));
    }

    //투표
    @PutMapping("/{boardID}/vote")
    public ResponseEntity<ResultDto> deleteVote(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("directoryID") int directoryID,
            @PathVariable("boardID") int boardID,
            @RequestBody @Valid UpdateVoteRequestDto requestDto
            ) {
        Board board = boardService.findBoardByBoardId(userPrincipal, boardID);
        boardService.updateVoteCount(board, requestDto.isAccept());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "투표 성공"));
    }

    //TODO: 퍼즐판 삭제 요청?
//    @DeleteMapping("/{boardID}/delete")
//    public ResponseEntity<ResultDto> deleteBoard(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
//            @PathVariable("directoryID") int directoryID,
//            @PathVariable("boardID") int boardID)
//    {
//
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(ResultDto.res(HttpStatus.OK.value(),
//                        "퍼즐판 삭제 성공"));
//    }


    //퍼즐판 내 전체 사진 조회
    @GetMapping("/{boardID}/images")
    public ResponseEntity<ResultDto> getBoardImages(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("directoryID") int directoryID,
            @PathVariable("boardID") int boardID)
    {
        Board board = boardService.findBoardByBoardId(userPrincipal, boardID);
        List<Piece> pieceList = boardService.findAllPhoto(boardID);

        List<FindPieceResponseDto> pieceDtoList = new ArrayList<>();
        for(Piece piece : pieceList) {
            FindPieceResponseDto pieceDto = FindPieceResponseDto.createPieceDto(piece.getImageUrl(), piece.getContent());
            pieceDtoList.add(pieceDto);
        }

        FindAllImageFromBoardResponseDto responseDto = FindAllImageFromBoardResponseDto
                .createFindAllImageFromBoardResponseDto(board.getThumbnailUrl(), pieceDtoList);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "퍼즐판 전체 사진 조회 성공", responseDto));
    }

    //썸네일 생성 및 수정
    @PutMapping("/{boardID}/thumbnails")
    public ResponseEntity<ResultDto> updateBoardThumbnails(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("directoryID") int directoryID,
            @PathVariable("boardID") int boardID,
            @RequestBody @Valid UpdateBoardThumbnailRequestDto requestDto
            ){

        Board board = boardService.findBoardByBoardId(userPrincipal, boardID);
        boardService.updateThumbnailUrl(board, requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "썸네일 수정 성공"));
    }
}
