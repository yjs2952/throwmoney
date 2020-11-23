package com.kakaopay.throwmoney.web.dto;

import lombok.*;

@Getter @Setter @ToString
public class ResponseThrowMoneyDto {
    private String token;

    @Builder
    public ResponseThrowMoneyDto(String token) {
        this.token = token;
    }
}
