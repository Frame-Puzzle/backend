package com.frazzle.main.domain.piece.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPieceResponseDto {

    private String missionName;
    private String imgUrl;
    private String comment;

    @Builder
    public FindPieceResponseDto(String missionName, String imgUrl, String comment) {
        this.missionName = missionName;
        this.imgUrl = imgUrl;
        this.comment = comment;
    }

    public static FindPieceResponseDto createPieceDto(String missionName, String imgUrl, String comment) {
        return FindPieceResponseDto.builder()
                .missionName(missionName)
                .imgUrl(imgUrl)
                .comment(comment)
                .build();
    }
}
