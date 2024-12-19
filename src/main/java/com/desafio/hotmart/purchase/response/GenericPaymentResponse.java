package com.desafio.hotmart.purchase.response;

public record GenericPaymentResponse<T extends PaymentDetails>(T data) { }