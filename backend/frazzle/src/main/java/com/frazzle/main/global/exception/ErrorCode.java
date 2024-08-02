package com.frazzle.main.global.exception;

import org.springframework.http.HttpStatus;

//에러 코드 모음집
public enum ErrorCode {
    UNAUTHORIZED("인증되지않은 요청입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("유효하지않은 액세스 토큰입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("유효하지않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
    BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    DUPLICATED_NICKNAME("중복된 닉네임입니다.", HttpStatus.BAD_REQUEST),
    NOT_EXIST_USER("존재하지 않는 유저입니다.", HttpStatus.UNAUTHORIZED),
    NOT_EXIST_FIREBASE_TOKEN("파이어베이스 토큰이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    FAILED_SEND_MESSAGE("메시지를 보내는데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_CONVERT_FILE("파일을 변환하는데 실패했습니다", HttpStatus.MULTI_STATUS),
    DENIED_UPDATE("수정 권한이 없습니다.", HttpStatus.FORBIDDEN),
    DENIED_FIND_MEMBER("멤버 조회 권한이 없습니다", HttpStatus.FORBIDDEN),
    DENIED_INVITE_MEMBER("멤버 초대 권한이 없습니다", HttpStatus.FORBIDDEN),
    DENIED_CANCEL_MEMBER("멤버 초대 취소 권한이 없습니다", HttpStatus.FORBIDDEN),
    NOT_EXIST_DIRECTORY("존재하지 않는 디렉토리입니다.", HttpStatus.NOT_FOUND),
    DUPLICATED_DIRECTORY_MEMBER("이미 초대된 회원입니다.", HttpStatus.BAD_REQUEST),
    NOT_DIRECTORY_MEMBER("해당 디렉토리에 접근 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    NOT_EXIST_BOARD("보드판이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_EXIST_PIECE("존재하지 않는 퍼즐조각입니다.", HttpStatus.NOT_FOUND),
    DENIED_UPDATE_PIECE("퍼즐을 처음 등록한 사용자만 수정할 수 있습니다.", HttpStatus.NOT_ACCEPTABLE),
    FAILED_CREATE_PIECE("퍼즐 조각 생성에 실패했습니다.", HttpStatus.NOT_FOUND),
    CANNOT_BE_NEGATIVE("난수에 음수값은 사용할 수 없습니다. ", HttpStatus.BAD_REQUEST)
    ;

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