package com.frazzle.main.domain.piece.controller;

import com.frazzle.main.domain.piece.dto.PieceDto;
import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.global.utils.ResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("directories/{directoryID}/boards/{boardID}/pieces/{pieceID}")
public class PieceController {

    //[PUT] 퍼즐 조각의 사진, 코멘트 생성 및 수정
    @PutMapping
    public ResponseEntity<ResultDto> updatePiece(
            @PathVariable("pieceID") int pieceID,
            UserPrincipal userPrincipal,
            PieceDto requestDto
            )
    {

        //response: 수정에 성공했습니다.
        return null;
    }

    //[GET] 퍼즐 조각 상세 조회
    @GetMapping
    public ResponseEntity<ResultDto> detailPiece(
            @PathVariable("pieceID") int pieceID,
            UserPrincipal userPrincipal
            )
    {

        //PieceDto 반환
        /*
        {
            "status": 200,
                "message": "조회에 성공했습니다.",
                "data": {
            "imgUrl":"s3 url",
                    "comment":"코멘트"
        }
        */
        return null;
    }
}
