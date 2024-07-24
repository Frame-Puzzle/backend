package com.frazzle.main.domain.directory.controller;

import com.frazzle.main.domain.directory.dto.CreateDirectoryRequestDto;
import com.frazzle.main.domain.directory.service.DirectoryService;
import com.frazzle.main.global.models.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directories")
@Slf4j
public class DirectoryController {

    private final DirectoryService directoryService;

    @PostMapping("")
    public ResponseEntity<?> createDirectory(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateDirectoryRequestDto requestDto) {

        directoryService.createDirectory(userPrincipal, requestDto);
        return ResponseEntity.ok().build();
    }
}
