package com.frazzle.main.domain.board.dto;

import com.frazzle.main.domain.piece.dto.PieceDto;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindAllImageFromBoardResponseDto {

    private String thumbnailUrl;

    private List<PieceDto> imgList;

    @Builder
    private FindAllImageFromBoardResponseDto(String thumbnailUrl, List<PieceDto> imgList) {
        this.thumbnailUrl = thumbnailUrl;
        this.imgList = imgList;
    }

    public static FindAllImageFromBoardResponseDto createFindAllImageFromBoardResponseDto(String thumbnailUrl, List<PieceDto> imgList) {
        return FindAllImageFromBoardResponseDto.builder()
                .thumbnailUrl(thumbnailUrl).imgList(imgList).build();
    }
}
