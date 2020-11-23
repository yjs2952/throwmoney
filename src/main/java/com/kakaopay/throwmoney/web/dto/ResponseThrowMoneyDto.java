package com.kakaopay.throwmoney.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ResponseThrowMoneyDto {
    private final String token;

    @Builder
    public ResponseThrowMoneyDto(String token) {
        this.token = token;
    }
}
