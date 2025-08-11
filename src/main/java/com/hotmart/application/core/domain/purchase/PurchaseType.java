package com.hotmart.application.core.domain.purchase;

import java.util.Arrays;

public enum PurchaseType {
    CREDIT_CARD(State.PROCESSED, true), PIX(State.WAIT, false), TICKET(State.WAIT, false);

    private final State initialState;
    private final boolean recurring;

    PurchaseType(State state, boolean recurring) {
        this.initialState = state;
        this.recurring = recurring;
    }

    public static PurchaseType getByName(String name) {
        return Arrays.stream(PurchaseType.values())
                .filter(p -> p.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean isSmartPaymentWithCreditCard(boolean smartPayment) {
        return smartPayment && isCreditCard();
    }

    private boolean isCreditCard() {
        return this == CREDIT_CARD;
    }
}