package com.kakaopay.throwmoney.service;

import com.kakaopay.throwmoney.domain.money.EventMoney;
import com.kakaopay.throwmoney.domain.money.EventMoneyRepository;
import com.kakaopay.throwmoney.domain.money.Money;
import com.kakaopay.throwmoney.domain.money.MoneyRepository;
import com.kakaopay.throwmoney.domain.money.strategy.DistributeMoneyStrategy;
import com.kakaopay.throwmoney.domain.money.strategy.GenerateTokenStrategy;
import com.kakaopay.throwmoney.web.dto.RequestThrowMoneyDto;
import com.kakaopay.throwmoney.web.dto.ResponseThrowMoneyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventMoneyService {

    private final EventMoneyRepository eventMoneyRepository;
    private final MoneyRepository moneyRepository;
    private final GenerateTokenStrategy generateTokenStrategy;
    private final DistributeMoneyStrategy distributeMoneyStrategy;

    @Transactional
    public ResponseThrowMoneyDto distributeMoney(RequestThrowMoneyDto params, Long userId, String roomId) {
        Money money = moneyRepository.findByMemberId(userId).orElseThrow(() -> new EntityNotFoundException("카카오 머니가 존재하지 않습니다"));
        String token = createToken(generateTokenStrategy);
        List<Long> distributedMoney = getDistributedMoney(params, distributeMoneyStrategy);
        return createEventMoneyList(distributedMoney, userId, roomId, money, token);
    }

    private String createToken(GenerateTokenStrategy strategy) {
        while (true) {
            String token = strategy.generate();
            if (!eventMoneyRepository.existsByToken(token)) {
                return token;
            }
        }
    }

    private List<Long> getDistributedMoney(RequestThrowMoneyDto params, DistributeMoneyStrategy strategy) {
        return strategy.distribute(params.getPrice(), params.getNumberOfTarget());
    }

    private ResponseThrowMoneyDto createEventMoneyList(List<Long> distributedMoney, Long userId, String roomId, Money money, String token) {
        List<EventMoney> saveEventMoneyList = new ArrayList<>();
        for (Long price : distributedMoney) {
            EventMoney eventMoney = EventMoney.createEventMoney(price, roomId, userId, money, token);
            saveEventMoneyList.add(eventMoney);
        }
        eventMoneyRepository.saveAll(saveEventMoneyList);

        return ResponseThrowMoneyDto.builder()
                .token(token)
                .build();
    }
}
