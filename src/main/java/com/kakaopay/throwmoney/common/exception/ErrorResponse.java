package com.kakaopay.throwmoney.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    private String code;
    private String message;
    private HttpStatus status;
    private String path;
    private List<String> errors;

    public int getStatus() {
        return status.value();
    }

}
