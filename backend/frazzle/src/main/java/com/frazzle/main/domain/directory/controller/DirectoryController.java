package com.frazzle.main.domain.directory.controller;

import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.domain.directory.dto.UserByEmailResponseDto;
import com.frazzle.main.domain.directory.dto.UpdateDirectoryNameRequestDto;
import com.frazzle.main.domain.directory.service.DirectoryService;
import com.frazzle.main.global.models.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directories")
@Slf4j
public class DirectoryController {

    private final DirectoryService directoryService;

    @PostMapping
    public ResponseEntity<?> createDirectory(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateDirectoryRequestDto requestDto) {

        directoryService.createDirectory(userPrincipal, requestDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{directoryId}")
    public ResponseEntity<?> updateDirectoryName(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateDirectoryNameRequestDto requestDto,
            @PathVariable int directoryId
    ){
        directoryService.updateDirectoryName(userPrincipal, requestDto, directoryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{directoryId}/users/find")
    public ResponseEntity<?> findUserByEmail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("directoryId") int directoryId,
            @RequestParam("email") String email) {
        List<UserByEmailResponseDto> responses = directoryService.findUserByEmail(userPrincipal, email, directoryId);
        return ResponseEntity.ok().body(responses);
    }
}
