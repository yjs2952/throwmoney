package com.kakaopay.throwmoney.domain.money;

import com.kakaopay.throwmoney.domain.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EventMoneyRepositoryTest {

    @Autowired
    private EventMoneyRepository eventMoneyRepository;

    @BeforeEach
    void setUp() {
        EventMoney eventMoney = EventMoney.builder()
                .money(new Money(1000L, new Member(" 무지"), null, null))
                .roomId("room1")
                .token("aaa")
                .build();
        eventMoneyRepository.save(eventMoney);
    }

    @ParameterizedTest
    @CsvSource(value = {"aaa,true", "aab,false"})
    void existsByTokenTest(String token, boolean expected) {
        boolean result = eventMoneyRepository.existsByToken(token);
        assertThat(result).isEqualTo(result);
    }

}