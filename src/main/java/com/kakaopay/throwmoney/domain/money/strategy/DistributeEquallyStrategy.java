package com.kakaopay.throwmoney.domain.money.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DistributeEquallyStrategy implements DistributeMoneyStrategy {

    @Override
    public List<Long> distribute(Long price, Integer numberOfMembers) {
        List<Long> eventMoneyList = new ArrayList<>();
        long quotient = price / numberOfMembers;
        long remainder = price % numberOfMembers;

        for (int i = 0; i < numberOfMembers; i++) {
            if (i < remainder) {
                eventMoneyList.add(quotient + 1);
                continue;
            }
            eventMoneyList.add(quotient);
        }
        return eventMoneyList;
    }
}
