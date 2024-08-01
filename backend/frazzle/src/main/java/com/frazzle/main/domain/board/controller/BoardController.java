package com.frazzle.main.domain.board.controller;

import com.frazzle.main.domain.board.dto.CreateBoardRequestDto;
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
@RequestMapping("/directories/{directoryID}/boards")
@CrossOrigin(origins = "*")
public class BoardController {

    private final BoardService boardService;

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

//    @PutMapping("")
//    public ResponseEntity<ResultDto> updateBoard(){}
//
//    @DeleteMapping("")
//    public ResponseEntity<ResultDto> deleteBoard(){}
}
