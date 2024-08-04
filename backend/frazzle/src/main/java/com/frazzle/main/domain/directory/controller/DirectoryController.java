package com.frazzle.main.domain.directory.controller;

import com.frazzle.main.domain.board.dto.CreateBoardRequestDto;
import com.frazzle.main.domain.board.dto.CreateBoardResponseDto;
import com.frazzle.main.domain.board.service.BoardService;
import com.frazzle.main.domain.directory.dto.*;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.service.DirectoryService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directories")
@CrossOrigin(origins = "*")
@Slf4j
public class DirectoryController {

    private final DirectoryService directoryService;
    private final BoardService boardService;

    @Operation(summary = "디렉토리 생성", description = "디렉토리 생성하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "생성에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @PostMapping
    public ResponseEntity<?> createDirectory(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateDirectoryRequestDto requestDto) {

        Directory directory = directoryService.createDirectory(userPrincipal, requestDto);
        CreateDirectoryResponseDto responseDto = CreateDirectoryResponseDto.CreateResponseDto(directory);

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "디렉토리 등록에 성공했습니다.", responseDto));
    }

    @Operation(summary = "디렉토리명 수정", description = "디렉토리명 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "수정에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @PutMapping("/{directoryId}")
    public ResponseEntity<?> updateDirectoryName(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateDirectoryNameRequestDto requestDto,
            @PathVariable int directoryId
    ){
        directoryService.updateDirectoryName(userPrincipal, requestDto, directoryId);
        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "디렉토리 이름을 수정하였습니다."));
    }

    @Operation(summary = "디렉토리 초대 멤버 여부 조회", description = "디렉토리에 멤버 초대 여부 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "조회에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @GetMapping("/{directoryId}/users/find")
    public ResponseEntity<?> findUserByEmail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("directoryId") int directoryId,
            @RequestParam("email") String email) {
        List<UserByEmailResponseDto> responses = directoryService.findUserByEmail(userPrincipal, email, directoryId);
        Map<String, Object> data = new HashMap<>();
        data.put("memberList", responses);
        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "조회에 성공했습니다", data));
    }

    @Operation(summary = "디렉토리의 멤버 초대", description = "디렉토리 내의 멤버 초대하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "초대에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "초대에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @PostMapping("/{directoryId}/user")
    public ResponseEntity<?> inviteMember(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("directoryId") int directoryId,
            @RequestBody InviteOrCancelMemberRequestDto requestDto
    ){
        directoryService.inviteMember(userPrincipal, requestDto, directoryId);
        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "초대에 성공했습니다."));
    }

    @Operation(summary = "디렉토리의 멤버 초대 취소", description = "디렉토리의 멤버 초대 취소하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "생성에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @DeleteMapping("/{directoryId}/user")
    public ResponseEntity<?> cancelMemberInvitation(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("directoryId") int directoryId,
            @RequestBody InviteOrCancelMemberRequestDto requestDto
    ){
        directoryService.cancelMemberInvitation(userPrincipal, requestDto, directoryId);
        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "초대 취소에 성공했습니다."));
    }

    @Operation(summary = "내가 참여한 디렉토리 찾기", description = "내가 참여한 디렉토리 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "조회에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @GetMapping("")
    public ResponseEntity<?> findMyDirectory(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(value = "category", required = false) String category
     ){
        List<FindMyDirectoryResponseDto> response = directoryService.findMyDirectory(userPrincipal, category);
        Map<String, Object> data = new HashMap<>();
        data.put("directoryList", response);
        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "조회에 성공했습니다.", data));
    }

    @Operation(summary = "퍼즐판 생성", description = "퍼즐판 생성하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "생성에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @PostMapping("/{directoryId}/boards")
    public ResponseEntity<ResultDto> createBoard(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("directoryId") int directoryId,
            @Valid @RequestBody CreateBoardRequestDto requestDto)
    {
        CreateBoardResponseDto responseDto = boardService.createBoard(userPrincipal, requestDto, directoryId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultDto.res(HttpStatus.OK.value(),
                        "퍼즐판 생성 성공", responseDto));
    }

    @Operation(summary = "디렉토리 세부 조회", description = "디렉토리 세부 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "조회에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @GetMapping("{directoryId}")
    public ResponseEntity<?> findDetailDirectory(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("directoryId") int directoryId
    ){
        DetailDirectoryResponsetDto response = directoryService.findDetailDirectory(userPrincipal, directoryId);
        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "조회에 성공했습니다.", response));
    }
}
