package com.frazzle.main.domain.user.dto;

import com.frazzle.main.domain.directory.dto.UserByEmailResponseDto;
import com.frazzle.main.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExistNicknameResponseDto {
    private Boolean isExistNickname;

    private ExistNicknameResponseDto(Boolean isExistNickname) {
        this.isExistNickname = isExistNickname;
    }

    public static ExistNicknameResponseDto createExistNickameResponseDto(Boolean isExist) {
        return new ExistNicknameResponseDto(isExist);
    }
}
