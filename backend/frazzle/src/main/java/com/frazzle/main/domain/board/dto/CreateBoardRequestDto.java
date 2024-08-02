package com.frazzle.main.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateBoardRequestDto {

    private String[] guide;

    private String[] keyword;

    //review : int형은 NotBlank보다 NotNull이 더 적합
    @NotNull
    private int boardSize;
}
