package com.kakaopay.throwmoney.common.exception;

import org.springframework.http.HttpStatus;

public enum CommonError {

    COMMON_BAD_REQUEST("요청 파라미터가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;
    private final String message;

    CommonError(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return this.message;
    }

    public int getStatus() {
        return this.httpStatus.value();
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
