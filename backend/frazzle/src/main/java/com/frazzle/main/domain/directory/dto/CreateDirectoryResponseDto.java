package com.frazzle.main.domain.directory.dto;

import com.frazzle.main.domain.directory.entity.Directory;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateDirectoryResponseDto {

    private int directoryId;

    @Builder
    private CreateDirectoryResponseDto(int directoryId) {
        this.directoryId = directoryId;
    }

    public static CreateDirectoryResponseDto CreateResponseDto(Directory directory) {
        return CreateDirectoryResponseDto.builder()
                .directoryId(directory.getDirectoryId())
                .build();
    }
}
