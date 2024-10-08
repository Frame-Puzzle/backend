package com.frazzle.main.domain.board.controller;

import com.frazzle.main.domain.board.dto.*;
import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.service.BoardService;
import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.global.utils.ResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/boards")
@Slf4j
public class BoardController {
    private final BoardService boardService;

    //퍼즐판 및 퍼즐 조각 전체 조회
    @Operation(summary = "퍼즐판 상세 조회", description = "퍼즐판과 퍼즐판 내의 퍼즐조각의 관한 정보 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "조회에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @GetMapping("/{boardID}")
    public ResponseEntity<ResultDto> findBoardAndPiece(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("boardID") int boardID)
    {
        FindBoardAndPiecesResponseDto responseDto = boardService.findBoardAndPieces(userPrincipal, boardID);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "퍼즐판 및 퍼즐조각들 조회 성공", responseDto));
    }

    //투표
    @Operation(summary = "퍼즐판삭제 투표하기", description = "퍼즐판 삭제를 위한 투표하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투표에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "투표에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @PutMapping("/{boardID}/vote")
    public ResponseEntity<ResultDto> deleteVote(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("boardID") int boardID,
            @RequestBody @Valid UpdateVoteRequestDto requestDto
            ) {

        log.info(String.valueOf(requestDto.isAccept()));

        boolean isDeletedBoard = boardService.updateVoteCount(userPrincipal, boardID, requestDto.isAccept());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        (isDeletedBoard ? "투표 및 삭제 성공" : "투표 성공")));
    }

    //퍼즐판 내 전체 사진 조회
    @Operation(summary = "퍼즐판 내 전체 사진 조회하기", description = "완료 퍼즐판에서 전체 사진을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "조회에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
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
    @Operation(summary = "퍼즐판 썸네일 사진 수정하기", description = "완료 퍼즐판에서 썸네일 사진을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "수정에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
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

    //썸네일 조회
    @GetMapping("/{boardID}/thumbnails")
    @Operation(summary = "퍼즐판 썸네일 사진 조회하기", description = "완료 퍼즐판에서 썸네일 사진을 조회하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "조회에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    public ResponseEntity<ResultDto> findBoardThumbnails(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("boardID") int boardID
    ){

        FindThumbnailResponseDto responseDto = boardService.findThumbnailUrl(userPrincipal, boardID);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "썸네일 조회 성공", responseDto));
    }

    //게임 대기방 생성시 대표 사진 조회
    @GetMapping("/{boardID}/games")
    @Operation(summary = "게임 대기방 생성시 대표 사진 조회", description = "게임 대기방 생성시 대표 사진 조회하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "조회에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    public ResponseEntity<ResultDto> findMaxPeople(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("boardID") int boardID) {

        FindMaxPeopleResponseDto responseDto = boardService.findMaxPeople(userPrincipal, boardID);


        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "조회에 성공했슴니다.", responseDto));
    }

    //게임 대기방 존재 여부 조회
    @Operation(summary = "게임 대기방 존재 여부 조회하기", description = "게임 대기방 존재 여부 조회하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "조회에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @GetMapping("/{boardID}/rooms")
    public ResponseEntity<ResultDto> findGameRoom(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("boardID") int boardID)
    {
        FindGameRoomResponseDto responseDto = boardService.findGameRoom(userPrincipal, boardID);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "게임 대기방 조회 성공", responseDto));
    }

    //인게임 존재 여부 조회
    @Operation(summary = "게임 진행중 존재 여부 조회하기", description = "게임 진행중 존재 여부 조회하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "조회에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @GetMapping("/{boardID}/games/in-game")
    public ResponseEntity<ResultDto> findInGame(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("boardID") int boardID)
    {
        FindInGameResponseDto responseDto = boardService.findInGame(userPrincipal, boardID);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "인게임 존재 여부 조회 성공", responseDto));
    }
}
