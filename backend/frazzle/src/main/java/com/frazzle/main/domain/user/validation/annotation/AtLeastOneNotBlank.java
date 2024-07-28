package com.frazzle.main.domain.user.validation.annotation;

import com.frazzle.main.domain.user.validation.validator.AtLeastOneNotBlankValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//유효성 검사하는 애노테이션으로 validatedBy의 뒤는 이걸 사용하는 클래스를 가리킴
@Constraint(validatedBy = { AtLeastOneNotBlankValidator.class })
//클래스나 인터페이스에 사용 가능
@Target({ ElementType.TYPE })
//언제까지 유지 하는지 런타임 시에도 가능
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneNotBlank {
    //실패 했을 때 발생
    String message() default "적어도 하나의 값은 있어야 합니다";
    //유효성 검사 타입 빈배열 전달
    Class<?>[] groups() default {};
    //추가로 전달할 정보로 빈배열
    Class<? extends Payload>[] payload() default {};
}
