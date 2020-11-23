package com.kakaopay.throwmoney.domain.money;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventMoneyRepository extends JpaRepository<EventMoney, Long> {

    boolean existsByToken(String token);
}
