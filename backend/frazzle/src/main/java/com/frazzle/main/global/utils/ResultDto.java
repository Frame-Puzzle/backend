package com.frazzle.main.global.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class ResultDto<T> {
    private int status;
    private String message;
    private T data;

    public ResultDto(final int status, final String message) {
        this.status = status;
        this.message = message;
        data = null;
    }

    public static <T> ResultDto<T> res(final int status, final String message) {
        return res(status, message, null);
    }

    public static <T> ResultDto<T> res(final int status, final String message, final T t) {
        return ResultDto.<T>builder()
                .data(t)
                .status(status)
                .message(message)
                .build();
    }

}
