package com.kakaopay.throwmoney.domain.money;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventMoneyRepository extends JpaRepository<EventMoney, Long> {

    boolean existsByToken(String token);

    Optional<EventMoney> findTopByTokenAndEventStatusAndEventTypeOrderByIdDesc(String token, EventStatus eventStatus, EventType eventType);

    Optional<List<EventMoney>> findAllByTokenAndEventStatusAndSearchableDateAfter(String token, EventStatus eventStatus, LocalDateTime searchableDate);
}
