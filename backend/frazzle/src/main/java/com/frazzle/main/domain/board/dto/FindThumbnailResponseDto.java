package com.frazzle.main.domain.board.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindThumbnailResponseDto {
    private String url;

    @Builder
    private FindThumbnailResponseDto(String url) {
        this.url = url;
    }

    public static FindThumbnailResponseDto createResponseDto(String url) {
        return FindThumbnailResponseDto.builder()
                .url(url)
                .build();
    }
}
