package com.kakaopay.throwmoney.domain.money;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class MoneyRepositoryTest {

    @Autowired
    private MoneyRepository moneyRepository;

    @Test
    @DisplayName("존재하지 않는 경우 예외 발생")
    void findByMemberIdTest(){
        Optional<Money> money = moneyRepository.findByMemberId(11L);

        assertThrows(EntityNotFoundException.class, () ->
            money.orElseThrow(EntityNotFoundException::new)
        );
    }
}