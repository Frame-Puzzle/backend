package com.frazzle.main.domain.board.dto;

import com.frazzle.main.domain.piece.dto.FindPieceResponseDto;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindAllImageFromBoardResponseDto {

    private String thumbnailUrl;

    private List<FindPieceResponseDto> imgList;

    @Builder
    private FindAllImageFromBoardResponseDto(String thumbnailUrl, List<FindPieceResponseDto> imgList) {
        this.thumbnailUrl = thumbnailUrl;
        this.imgList = imgList;
    }

    public static FindAllImageFromBoardResponseDto createFindAllImageFromBoardResponseDto(String thumbnailUrl, List<FindPieceResponseDto> imgList) {
        return FindAllImageFromBoardResponseDto.builder()
                .thumbnailUrl(thumbnailUrl).imgList(imgList).build();
    }
}
