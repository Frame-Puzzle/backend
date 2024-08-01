package com.frazzle.main.global.exception;

import com.frazzle.main.global.utils.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResultDto<Object>> handleCustomException(CustomException ex) {
        logger.error("[CustomException] errorCode: " + ex.getErrorCode());
        logger.error("[CustomException] message: " + ex.getMessage());

        ResultDto<Object> response = ResultDto.res(
                ex.getErrorCode().getHttpStatus().value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(response, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResultDto<Object>> handleRuntimeException(RuntimeException ex) {
        logger.error("[RuntimeException] message: " + ex.getMessage());

        ResultDto<Object> response = ResultDto.res(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultDto<Object>> handleException(Exception ex) {
        logger.error("[Exception] message: " + ex.getMessage());

        ResultDto<Object> response = ResultDto.res(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
