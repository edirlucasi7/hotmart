package com.hotmart.infrastructure.adapter.in.purchase;

public record GenericPaymentResponse<T extends PaymentDetails>(T data) { }