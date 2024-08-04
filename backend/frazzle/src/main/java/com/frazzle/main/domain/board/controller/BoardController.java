package com.frazzle.main.domain.board.controller;

import com.frazzle.main.domain.board.dto.*;
import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.service.BoardService;
import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.global.utils.ResultDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    //퍼즐판 및 퍼즐조각 전체 조회
    @GetMapping("/{boardID}")
    public ResponseEntity<ResultDto> findBoardAndPiece(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("boardID") int boardID)
    {
        FindBoardAndPiecesResponseDto responseDto =boardService.findBoardAndPieces(userPrincipal, boardID);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "퍼즐판 및 퍼즐조각들 조회 성공", responseDto));
    }

    //투표
    @PutMapping("/{boardID}/vote")
    public ResponseEntity<ResultDto> deleteVote(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("boardID") int boardID,
            @RequestBody @Valid UpdateVoteRequestDto requestDto
            ) {
        boolean isDeletedBoard = boardService.updateVoteCount(userPrincipal, boardID, requestDto.isAccept());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        (isDeletedBoard ? "투표 및 삭제 성공" : "투표 성공")));
    }

    //퍼즐판 내 전체 사진 조회
    @GetMapping("/{boardID}/images")
    public ResponseEntity<ResultDto> getBoardImages(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("boardID") int boardID)
    {

        FindAllImageFromBoardResponseDto responseDto = boardService.findAllPhoto(userPrincipal, boardID);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "퍼즐판 전체 사진 조회 성공", responseDto));
    }

    //썸네일 생성 및 수정
    @PutMapping("/{boardID}/thumbnails")
    public ResponseEntity<ResultDto> updateBoardThumbnails(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("boardID") int boardID,
            @RequestBody @Valid UpdateBoardThumbnailRequestDto requestDto
            ){

        boardService.updateThumbnailUrl(userPrincipal, boardID, requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "썸네일 수정 성공"));
    }
}
