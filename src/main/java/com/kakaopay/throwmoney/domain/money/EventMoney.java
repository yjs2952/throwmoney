package com.kakaopay.throwmoney.domain.money;

import com.kakaopay.throwmoney.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
public class EventMoney extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 3, nullable = false)
    private String token;

    private Long amount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "money_id")
    private Money money;

    private String roomId;

    private LocalDateTime expiredDate;
    private LocalDateTime searchableDate;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private Long createId;
    private Long modifyId;

    @Builder
    public EventMoney(String token, Long amount, Money money, String roomId, LocalDateTime expiredDate, LocalDateTime searchableDate, EventStatus eventStatus, EventType eventType, Long createId, Long modifyId) {
        this.token = token;
        this.amount = amount;
        this.money = money;
        this.roomId = roomId;
        this.expiredDate = expiredDate;
        this.searchableDate = searchableDate;
        this.eventStatus = eventStatus;
        this.eventType = eventType;
        this.createId = createId;
        this.modifyId = modifyId;
    }

    public static EventMoney createEventMoney(Long price, String roomId, Long userId, Money money, String token) {
        return EventMoney.builder()
                .amount(price)
                .eventStatus(EventStatus.WAITING)
                .eventType(EventType.THROW)
                .expiredDate(LocalDateTime.now().plusMinutes(10))
                .searchableDate(LocalDateTime.now().plusDays(7))
                .token(token)
                .money(money)
                .roomId(roomId)
                .createId(userId)
                .modifyId(userId)
                .build();
    }
}
