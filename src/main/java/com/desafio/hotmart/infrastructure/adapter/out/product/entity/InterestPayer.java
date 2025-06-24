package com.desafio.hotmart.infrastructure.adapter.out.product.entity;

import java.util.Arrays;

public enum InterestPayer {

    BUYER, INFO_PRODUCER;

    public static InterestPayer fromName(String name) {
        return Arrays.stream(values()).filter(type -> type.name().equals(name)).findFirst().orElse(BUYER);
    }
}