package com.kakaopay.throwmoney.domain.money;

import com.kakaopay.throwmoney.domain.BaseTimeEntity;
import com.kakaopay.throwmoney.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Money extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "money_id")
    private Long id;

    private Long totalAmount;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long createId;
    private Long modifyId;

    @Builder
    public Money(Long totalAmount, Member member, Long createId, Long modifyId) {
        this.totalAmount = totalAmount;
        this.member = member;
        this.createId = createId;
        this.modifyId = modifyId;
    }

    public void addMoney(Long price){
        this.totalAmount += price;
    }

    public void minusMoney(Long price){
        this.totalAmount -= price;
    }
}
