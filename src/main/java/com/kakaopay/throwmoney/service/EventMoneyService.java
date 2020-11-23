package com.kakaopay.throwmoney.service;

import com.kakaopay.throwmoney.domain.money.*;
import com.kakaopay.throwmoney.domain.money.strategy.DistributeMoneyStrategy;
import com.kakaopay.throwmoney.domain.money.strategy.GenerateTokenStrategy;
import com.kakaopay.throwmoney.web.dto.RequestReceiveMoneyDto;
import com.kakaopay.throwmoney.web.dto.RequestThrowMoneyDto;
import com.kakaopay.throwmoney.web.dto.ResponseReceiveMoneyDto;
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
        String token = createToken(generateTokenStrategy);
        List<Long> distributedMoney = getDistributedMoney(params, distributeMoneyStrategy);
        Money money = moneyRepository.findByMemberId(userId).orElseThrow(() -> new EntityNotFoundException("카카오 머니가 존재하지 않습니다."));
        money.minusMoney(params.getPrice());
        return createEventMoneyList(distributedMoney, userId, roomId, token);
    }

    @Transactional
    public ResponseReceiveMoneyDto receiveMoney(RequestReceiveMoneyDto params, Long userId, String roomId) {
        EventMoney eventMoney = eventMoneyRepository.findTopByTokenAndEventStatusAndEventTypeOrderByIdDesc(params.getToken(), EventStatus.WAITING, EventType.THROW).orElseThrow(() -> new EntityNotFoundException("받을 수 있는 머니가 존재하지 않습니다."));
        eventMoney.receiveMoney(userId, roomId);

        Money money = moneyRepository.findByMemberId(userId).orElseThrow(() -> new EntityNotFoundException("카카오 머니가 존재하지 않습니다."));
        money.addMoney(eventMoney.getPrice());
        return new ResponseReceiveMoneyDto(eventMoney.getPrice());
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

    private ResponseThrowMoneyDto createEventMoneyList(List<Long> distributedMoney, Long userId, String roomId, String token) {
        Money money = moneyRepository.findByMemberId(userId).orElseThrow(() -> new EntityNotFoundException("카카오 머니가 존재하지 않습니다"));

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
