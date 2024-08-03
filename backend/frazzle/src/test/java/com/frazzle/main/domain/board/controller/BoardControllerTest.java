package com.frazzle.main.domain.board.controller;

import com.frazzle.main.domain.board.InitializeSetting;
import com.frazzle.main.domain.directory.dto.UpdateDirectoryNameRequestDto;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.directory.service.DirectoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardControllerTest extends InitializeSetting {

    @BeforeEach
    void beforeEach(){
        //WARNING: DB 만들어져 있으면 건들지 말 것
        //init();
    }

    @AfterEach
    void afterEach(){clearAll();}

    @Test
    void createBoard() {
        myUserSetting();
        //Directory directory = directoryRepository.findByDirectoryId(45).get();
        UpdateDirectoryNameRequestDto dto = new UpdateDirectoryNameRequestDto("잉어킹");
        directoryService.updateDirectoryName(myUserPrincipal,dto, 45);


    }

    @Test
    void findBoardAndPiece() {
    }

    @Test
    void deleteVote() {
    }

    @Test
    void getBoardImages() {
    }

    @Test
    void updateBoardThumbnails() {
    }
}