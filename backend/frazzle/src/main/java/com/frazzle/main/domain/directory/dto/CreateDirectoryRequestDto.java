package com.frazzle.main.domain.directory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateDirectoryRequestDto {

    @NotBlank
    private String category;

    @NotBlank
    private String directoryName;

    public void changeKoreanCategory(String category) {
        this.category = category;
    }
}
