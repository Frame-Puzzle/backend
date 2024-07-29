package com.frazzle.main.global.exception;

import org.springframework.http.HttpStatus;

//에러 코드 모음집
public enum ErrorCode {
    UNAUTHORIZED("인증되지않은 요청입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("유효하지않은 액세스 토큰입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("유효하지않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
    BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    NOT_EXIST_USER("존재하지 않는 유저입니다.", HttpStatus.UNAUTHORIZED),
    DENIED_UPDATE("수정 권한이 없습니다.", HttpStatus.FORBIDDEN),
    DENIED_FIND_MEMBER("멤버 조회 권한이 없습니다", HttpStatus.FORBIDDEN),
    DENIED_INVITE_MEMBER("멤버 초대 권한이 없습니다", HttpStatus.FORBIDDEN),
    NOT_EXIST_DIRECTORY("존재하지 않는 디렉토리입니다.", HttpStatus.NOT_FOUND),
    DUPLICATED_DIRECTORY_MEMBER("이미 초대된 회원입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}