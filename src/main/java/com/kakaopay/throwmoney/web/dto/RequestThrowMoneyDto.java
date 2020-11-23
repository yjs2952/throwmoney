package com.kakaopay.throwmoney.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class RequestThrowMoneyDto {

    @NotNull
    @Positive
    private Long price;

    @NotNull
    @Positive
    private Integer numberOfTarget;

    @Builder
    public RequestThrowMoneyDto(Long price, Integer numberOfTarget) {
        this.price = price;
        this.numberOfTarget = numberOfTarget;
    }
}
