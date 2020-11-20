package com.kakaopay.throwmoney.kakaomoney.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
public class EventMoney extends BaseTimeEntity{

    private Long id;
    private String token;
    private Long price;
    private User user;
    private LocalDateTime eventDate;
    private LocalDateTime expiredDate;
    private LocalDateTime searchableDate;
    private String status;
    private String type;
}
