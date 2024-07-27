package com.frazzle.main.global.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
//프론트로 반환 시에 status, message, data의 값을 일정하게 보내주기 위한 클래스
public class ResultDto<T> {
    //상태 코드
    private int status;
    //메시지
    private String message;
    //프론트에서 사용할 데이터 값
    private T data;

    public ResultDto(final int status, final String message) {
        this.status = status;
        this.message = message;
        data = null;
    }

    //상태코드와 메시지만 보내는 용도
    public static <T> ResultDto<T> res(final int status, final String message) {
        return res(status, message, null);
    }

    //data값까지 보내는 경우
    public static <T> ResultDto<T> res(final int status, final String message, final T t) {
        return ResultDto.<T>builder()
                .data(t)
                .status(status)
                .message(message)
                .build();
    }

}
