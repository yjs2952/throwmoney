package com.kakaopay.throwmoney.domain.money;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoneyRepository extends JpaRepository<Money, Long> {
    Optional<Money> findByMemberId(Long memberId);
}
