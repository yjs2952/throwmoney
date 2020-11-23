package com.kakaopay.throwmoney.service;

import com.kakaopay.throwmoney.domain.member.Member;
import com.kakaopay.throwmoney.domain.money.EventMoneyRepository;
import com.kakaopay.throwmoney.domain.money.Money;
import com.kakaopay.throwmoney.domain.money.MoneyRepository;
import com.kakaopay.throwmoney.domain.money.strategy.DistributeMoneyStrategy;
import com.kakaopay.throwmoney.domain.money.strategy.GenerateTokenStrategy;
import com.kakaopay.throwmoney.web.dto.RequestThrowMoneyDto;
import com.kakaopay.throwmoney.web.dto.ResponseThrowMoneyDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventMoneyServiceTest {

    @InjectMocks
    private EventMoneyService eventMoneyService;

    @Mock
    private EventMoneyRepository eventMoneyRepository;

    @Mock
    private MoneyRepository moneyRepository;

    @Mock
    private GenerateTokenStrategy generateTokenStrategy;

    @Mock
    private DistributeMoneyStrategy distributeMoneyStrategy;

    @Test
    void hasMoneyByUserIdTest(){

        RequestThrowMoneyDto dto = RequestThrowMoneyDto.builder()
                .numberOfTarget(5)
                .price(10000L)
                .build();

        Long userId = 1L;
        String roomId = "room1";
        String token = "abc";

        when(moneyRepository.findByMemberId(userId)).thenReturn(Optional.of(new Money(10000L, new Member("라이언"), null, null)));
        when(generateTokenStrategy.generate()).thenReturn(token);

        ResponseThrowMoneyDto result = ResponseThrowMoneyDto.builder()
                .token(token)
                .build();

        ResponseThrowMoneyDto responseThrowMoneyDto = eventMoneyService.distributeMoney(dto, userId, roomId);
        assertThat(responseThrowMoneyDto).isEqualToComparingFieldByField(result) ;
    }
}