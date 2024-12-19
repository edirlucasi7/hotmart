package com.desafio.hotmart.purchase.response;

public record PixPaymentResponseDTO(String paymentCode, String message) implements PaymentDetails { }
