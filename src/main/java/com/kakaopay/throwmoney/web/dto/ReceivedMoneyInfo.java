package com.kakaopay.throwmoney.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ReceivedMoneyInfo {

    private Long price;
    private Long userId;

    @Builder
    public ReceivedMoneyInfo(Long price, Long userId) {
        this.price = price;
        this.userId = userId;
    }
}
