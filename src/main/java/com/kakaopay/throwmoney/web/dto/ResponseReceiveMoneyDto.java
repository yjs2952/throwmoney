package com.kakaopay.throwmoney.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseReceiveMoneyDto {

    private final Long price;

    @Builder
    public ResponseReceiveMoneyDto(Long price) {
        this.price = price;
    }
}
