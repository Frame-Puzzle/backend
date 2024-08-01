package com.frazzle.main.domain.piece.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdatePieceRequestDto {

    @NotBlank
    private MultipartFile imgFile;

    private String comment;
}
