package com.kakaopay.throwmoney.domain.money.strategy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DistributeEquallyStrategy.class)
class DistributeEquallyStrategyTest {

    @Autowired
    private DistributeMoneyStrategy strategy;

    @ParameterizedTest
    @MethodSource("provideInputs")
    void distributeTest(Long price, Integer numberOfMembers, List<Long> expected) {
        List<Long> result = strategy.distribute(price, numberOfMembers);

        assertThat(result).containsExactlyElementsOf(expected);
    }

    private static Stream<Arguments> provideInputs() {
        return Stream.of(
                Arguments.of(10000L, 5, Arrays.asList(2000L, 2000L, 2000L, 2000L, 2000L))
                ,Arguments.of(10000L, 3, Arrays.asList(3334L, 3333L, 3333L))
                ,Arguments.of(10000L, 6, Arrays.asList(1667L, 1667L, 1667L, 1667L, 1666L, 1666L))
        );
    }
}