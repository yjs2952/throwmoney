package com.kakaopay.throwmoney.kakaomoney.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
public class KakaoMoney extends BaseTimeEntity {
    private Long id;
    private Long totalAmount;
    private User user;
}
