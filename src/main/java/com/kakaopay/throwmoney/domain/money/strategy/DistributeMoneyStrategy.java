package com.kakaopay.throwmoney.domain.money.strategy;

import java.util.List;


@FunctionalInterface
public interface DistributeMoneyStrategy {

    List<Long> distribute(Long price, Integer numberOfMembers);
}
