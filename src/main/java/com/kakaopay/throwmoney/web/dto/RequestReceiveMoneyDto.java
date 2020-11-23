package com.kakaopay.throwmoney.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class RequestReceiveMoneyDto {

    @NotEmpty
    private String token;

    @Builder
    public RequestReceiveMoneyDto(@NotEmpty String token) {
        this.token = token;
    }
}
