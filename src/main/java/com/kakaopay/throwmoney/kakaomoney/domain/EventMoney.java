package com.kakaopay.throwmoney.kakaomoney.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
public class EventMoney extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 3, nullable = false)
    private String token;

    private Long price;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "money_id")
    private Money money;

    private String roomId;

    private LocalDateTime expiredDate;
    private LocalDateTime searchableDate;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Builder
    public EventMoney(String token, Long price, Money money, String roomId, LocalDateTime expiredDate, LocalDateTime searchableDate, EventStatus eventStatus, EventType eventType) {
        this.token = token;
        this.price = price;
        this.money = money;
        this.roomId = roomId;
        this.expiredDate = expiredDate;
        this.searchableDate = searchableDate;
        this.eventStatus = eventStatus;
        this.eventType = eventType;
    }
}
