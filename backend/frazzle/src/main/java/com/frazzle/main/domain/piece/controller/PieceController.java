package com.frazzle.main.domain.piece.controller;

import com.frazzle.main.domain.piece.dto.FindPieceResponseDto;
import com.frazzle.main.domain.piece.dto.UpdatePieceRequestDto;
import com.frazzle.main.domain.piece.service.PieceService;
import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.global.utils.ResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pieces/{pieceID}")
@CrossOrigin(origins = "*")
public class PieceController {

    private final PieceService pieceService;

    //[PUT] 퍼즐 조각의 사진, 코멘트 생성 및 수정
    @Operation(summary = "퍼즐 조각 생성 및 수정", description = "퍼즐 조각의 사진, 코멘트 생성 및 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "생성에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @PutMapping
    public ResponseEntity<ResultDto> updatePiece(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("pieceID") int pieceID,
            @RequestParam(name = "imgFile",required = false) MultipartFile imgFile,
            @RequestParam(name = "comment", required = false) String comment
            )
    {
        boolean isCompleteBoard = pieceService.updatePiece(userPrincipal, pieceID, imgFile, comment);

        String message = "퍼즐 조각 수정 성공";
        if(isCompleteBoard) {
            message += " 및 퍼즐판 완성";
        }


        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        message));
    }

    //[GET] 퍼즐 조각 상세 조회
    @Operation(summary = "퍼즐 조각 상세 조회", description = "퍼즐 조각 상세 조회하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "조회에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @GetMapping
    public ResponseEntity<ResultDto> detailPiece(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("pieceID") int pieceID
            )
    {
        FindPieceResponseDto responseDto = pieceService.findPieceByPieceId(userPrincipal, pieceID);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "퍼즐 조각 조회 성공", responseDto));
    }
}
