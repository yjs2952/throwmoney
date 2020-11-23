package com.kakaopay.throwmoney.domain.money.strategy;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GenerateRandomTokenStrategy implements GenerateTokenStrategy {

    @Override
    public String generate() {
        StringBuilder token = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int rIndex = random.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    token.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    // A-Z
                    token.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    // 0-9
                    token.append((random.nextInt(10)));
                    break;
            }
        }
        return token.toString();
    }
}
