package com.desafio.hotmart.application.core.domain.purchase;

import java.util.Arrays;

// TODO tem que estar persistido no banco de dados
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

    public State getState() {
        return initialState;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public int setNumberOfInstallments(int numberOfInstallments) {
        if (!this.isRecurring() || numberOfInstallments == 0) return 1;
        return numberOfInstallments;
    }

    public boolean isCreditCard() {
        return this == CREDIT_CARD;
    }
}