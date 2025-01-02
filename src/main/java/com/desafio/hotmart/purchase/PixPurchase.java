package com.desafio.hotmart.purchase;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
public class PixPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private Purchase purchase;
    // TODO ver como deixar esse codigo sempre único
    @NotBlank
    private String codeToPay;
    private int confirmationTime;
    private boolean confirmed;

    @Deprecated
    public PixPurchase() { }

    private PixPurchase(Purchase purchase, int confirmationTime, String codeToPay) {
        this.purchase = purchase;
        this.confirmationTime = confirmationTime;
        this.codeToPay = codeToPay;
    }

    public static PixPurchase create(Purchase purchase, int confirmationTime, String codeToPay) {
        return new PixPurchase(purchase, confirmationTime, codeToPay);
    }

    public Long getId() {
        return id;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public String getCodeToPay() {
        return codeToPay;
    }

    public int getConfirmationTime() {
        return confirmationTime;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void updateConfirmationTime(int confirmationTime) {
        if (confirmed) return;
        this.confirmationTime = confirmationTime;
    }

    public Purchase confirmPayment() {
        this.confirmed = true;
        return this.purchase.process();
    }

    public boolean shouldSkipConfirmation() {
        return this.isConfirmed()
                || !this.hasValidConfirmationTime()
                || !this.isAPixWaitOnBold();
    }

    private boolean hasValidConfirmationTime() {
        return LocalDateTime.now().isBefore(purchase.getCreatedAt().plusMinutes(confirmationTime));
    }

    private boolean isAPixWaitOnBold() {
        return purchase.isWait();
    }
}
