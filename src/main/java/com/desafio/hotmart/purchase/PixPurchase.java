package com.desafio.hotmart.purchase;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class PixPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private Purchase purchase;
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
}
