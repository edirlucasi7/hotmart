package com.desafio.hotmart.infrastructure.adapter.in.purchase;

public record GenericPaymentResponse<T extends PaymentDetails>(T data) { }