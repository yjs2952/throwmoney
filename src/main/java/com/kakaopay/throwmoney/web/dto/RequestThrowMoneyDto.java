package com.kakaopay.throwmoney.web.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter @ToString
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
