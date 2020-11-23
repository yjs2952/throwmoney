package com.kakaopay.throwmoney.service;

import com.kakaopay.throwmoney.domain.money.*;
import com.kakaopay.throwmoney.domain.money.strategy.DistributeMoneyStrategy;
import com.kakaopay.throwmoney.domain.money.strategy.GenerateTokenStrategy;
import com.kakaopay.throwmoney.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public ResponseEventMoneyDto getEventMoneyList(RequestEventMoneyDto params, Long userId, String roomId) {
        if (!eventMoneyRepository.existsByToken(params.getToken())) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        List<EventMoney> eventMoneyList = eventMoneyRepository.findAllByTokenAndEventStatusAndSearchableDateAfter(params.getToken(), EventStatus.DONE, LocalDateTime.now()).orElseThrow(() -> new EntityNotFoundException("정보가 없습니다."));

        if (hasOtherUserMoney(userId, eventMoneyList)) {
            throw new IllegalArgumentException("다른 사람의 뿌리기 머니를 조회할 수 없습니다.");
        }

        return getResponseEventMoneyDto(userId, eventMoneyList);
    }

    private ResponseEventMoneyDto getResponseEventMoneyDto(Long userId, List<EventMoney> eventMoneyList) {
        EventMoney eventMoney = eventMoneyList.get(0);
        long throwMoney = eventMoneyList.stream().mapToLong(EventMoney::getPrice).sum();
        long totalReceivedPrice = eventMoneyList.stream().filter(em -> em.getEventStatus() == EventStatus.DONE).mapToLong(EventMoney::getPrice).sum();

        return ResponseEventMoneyDto.builder()
                .createDate(eventMoney.getCreateDate())
                .createId(userId)
                .throwPrice(throwMoney)
                .totalReceivedPrice(totalReceivedPrice)
                .receivedMoneyInfo(getReceivedMoneyInfos(eventMoneyList))
                .build();
    }

    private List<ReceivedMoneyInfo> getReceivedMoneyInfos(List<EventMoney> eventMoneyList) {
        return eventMoneyList.stream()
                    .map(em -> ReceivedMoneyInfo.builder()
                            .price(em.getPrice())
                            .userId(em.getModifyId())
                            .build()
                    ).collect(Collectors.toList());
    }

    private boolean hasOtherUserMoney(Long userId, List<EventMoney> eventMoneyList) {
        return eventMoneyList.stream().anyMatch(em -> !em.getCreateId().equals(userId));
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
