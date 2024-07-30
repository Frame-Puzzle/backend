package com.frazzle.main.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateBoardRequestDto {

    private String guide;

    private String keyword;

    @NotBlank
    private int boardSize;
}
