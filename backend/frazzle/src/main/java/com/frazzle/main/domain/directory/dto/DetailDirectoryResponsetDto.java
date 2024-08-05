package com.frazzle.main.domain.directory.dto;

import com.frazzle.main.domain.directory.entity.Directory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DetailDirectoryResponsetDto {

    private int directoryId;
    private String category;
    private String directoryName;
    private boolean isCurrentBoard;
    private List<MemberListDto> memberList;
    private List<BoardListDto> boardList;

    @Builder
    private DetailDirectoryResponsetDto(int directoryId, String category, String directoryName, boolean isCurrentBoard, List<MemberListDto> memberList, List<BoardListDto> boardList) {
        this.directoryId = directoryId;
        this.category = category;
        this.directoryName = directoryName;
        this.isCurrentBoard = isCurrentBoard;
        this.memberList = memberList;
        this.boardList = boardList;
    }

    public static DetailDirectoryResponsetDto createDetailDirectoryRequestDto(Directory directory, boolean isCurrentBoard, List<MemberListDto> memberList, List<BoardListDto> boardList) {
        return DetailDirectoryResponsetDto.builder()
                .directoryId(directory.getDirectoryId())
                .category(directory.getCategory())
                .directoryName(directory.getDirectoryName())
                .isCurrentBoard(isCurrentBoard)
                .memberList(memberList)
                .boardList(boardList)
                .build();
    }
}
