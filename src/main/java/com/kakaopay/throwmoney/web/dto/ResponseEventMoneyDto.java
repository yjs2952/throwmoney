package com.kakaopay.throwmoney.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponseEventMoneyDto {

    private LocalDateTime createDate;
    private Long throwPrice;
    private Long totalReceivedPrice;
    private Long createId;
    private List<ReceivedMoneyInfo> receivedMoneyInfo;

    @Builder
    public ResponseEventMoneyDto(LocalDateTime createDate, Long throwPrice, Long totalReceivedPrice, List<ReceivedMoneyInfo> receivedMoneyInfo, Long createId) {
        this.createDate = createDate;
        this.throwPrice = throwPrice;
        this.totalReceivedPrice = totalReceivedPrice;
        this.receivedMoneyInfo = receivedMoneyInfo;
        this.createId = createId;
    }
}
