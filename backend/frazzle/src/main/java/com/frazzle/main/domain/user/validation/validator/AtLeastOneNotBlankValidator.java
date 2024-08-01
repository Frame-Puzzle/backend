package com.frazzle.main.domain.user.validation.validator;

import com.frazzle.main.domain.user.dto.UpdateUserRequestDto;
import com.frazzle.main.domain.user.validation.annotation.AtLeastOneNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneNotBlankValidator implements ConstraintValidator<AtLeastOneNotBlank, UpdateUserRequestDto> {

    @Override
    public void initialize(AtLeastOneNotBlank constraintAnnotation) {
        // 초기화 로직이 필요한 경우 추가
    }

    @Override
    //유효성 검사로 context는 메시지나 추가 정보를 사용할 때 사용
    public boolean isValid(UpdateUserRequestDto value, ConstraintValidatorContext context) {
//        return (value.getNickname() != null && !value.getNickname().isBlank()) ||
//               (value.getProfileImg() != null && !value.getProfileImg().isEmpty());
        return true;
    }
}