package com.kakaopay.throwmoney.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class RequestEventMoneyDto {

    @NotEmpty
    private String token;

    @Builder
    public RequestEventMoneyDto(String token) {
        this.token = token;
    }
}
