package com.frazzle.main.domain.board.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindAllImageFromBoardResponseDto {

    private String thumbnailUrl;

    private FindPieceDto[] imgList;

    @Builder
    private FindAllImageFromBoardResponseDto(String thumbnailUrl, FindPieceDto[] imgList) {
        this.thumbnailUrl = thumbnailUrl;
        this.imgList = imgList;
    }

    public static FindAllImageFromBoardResponseDto createFindAllImageFromBoardResponseDto(String thumbnailUrl, FindPieceDto[] imgList) {
        return FindAllImageFromBoardResponseDto.builder()
                .thumbnailUrl(thumbnailUrl).imgList(imgList).build();
    }
}
