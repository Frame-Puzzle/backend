package com.frazzle.main.domain.piece.controller;

import com.frazzle.main.domain.piece.dto.FindPieceResponseDto;
import com.frazzle.main.domain.piece.dto.UpdatePieceRequestDto;
import com.frazzle.main.domain.piece.service.PieceService;
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
@RequestMapping("directories/{directoryID}/boards/{boardID}/pieces/{pieceID}")
@CrossOrigin(origins = "*")
public class PieceController {

    private final PieceService pieceService;

    //[PUT] 퍼즐 조각의 사진, 코멘트 생성 및 수정
    @PutMapping
    public ResponseEntity<ResultDto> updatePiece(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("directoryID") int directoryID,
            @PathVariable("pieceID") int pieceID,
            @Valid @RequestBody UpdatePieceRequestDto requestDto
            )
    {
        pieceService.updatePiece(userPrincipal, directoryID, pieceID, requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "퍼즐 조각 수정 성공"));
    }

    //[GET] 퍼즐 조각 상세 조회
    @GetMapping
    public ResponseEntity<ResultDto> detailPiece(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("directoryID") int directoryID,
            @PathVariable("pieceID") int pieceID
            )
    {
        FindPieceResponseDto responseDto = pieceService.findPieceByPieceId(userPrincipal, directoryID, pieceID);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "퍼즐 조각 조회 성공", responseDto));
    }
}
