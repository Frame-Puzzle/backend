package com.frazzle.main.domain.directory.dto;

import com.frazzle.main.domain.directory.entity.Directory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FindMyDirectoryResponseDto {

    private int directoryId;
    private LocalDateTime modifiedAt;
    private String directoryName;
    private String category;

    @Builder
    private FindMyDirectoryResponseDto(int directoryId, LocalDateTime modifiedAt, String directoryName, String category) {
        this.directoryId = directoryId;
        this.modifiedAt = modifiedAt;
        this.directoryName = directoryName;
        this.category = category;
    }

    public static FindMyDirectoryResponseDto createFindMyDirectoryResponseDto(Directory directory) {
        return FindMyDirectoryResponseDto.builder()
                .directoryId(directory.getDirectoryId())
                .modifiedAt(directory.getModifiedAt())
                .directoryName(directory.getDirectoryName())
                .category(directory.getCategory())
                .build();
    }
}
