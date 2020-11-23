package com.kakaopay.throwmoney.domain.money;

import com.kakaopay.throwmoney.domain.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EventMoneyRepositoryTest {

    @Autowired
    private EventMoneyRepository eventMoneyRepository;

    private String token;

    @BeforeEach
    void setUp() {
        token = "aaa";

        EventMoney eventMoney = EventMoney.builder()
                .money(new Money(10000L, new Member(" 무지"), 1L, 1L))
                .roomId("room1")
                .token(token)
                .eventStatus(EventStatus.WAITING)
                .eventType(EventType.THROW)
                .createId(1L)
                .modifyId(1L)
                .build();
        eventMoneyRepository.save(eventMoney);
    }

    @ParameterizedTest
    @CsvSource(value = {"aaa,true", "aab,false"})
    @DisplayName("토큰 별 이벤트 머니 존재 여부 테스트")
    void existsByTokenTest(String token, boolean expected) {
        boolean result = eventMoneyRepository.existsByToken(token);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findEventMoneyTest() {
        Optional<EventMoney> eventMoney = eventMoneyRepository.findTopByTokenAndEventStatusAndEventTypeOrderByIdDesc(token, EventStatus.WAITING, EventType.THROW);
        assertThat(eventMoney.isPresent()).isTrue();
    }
}