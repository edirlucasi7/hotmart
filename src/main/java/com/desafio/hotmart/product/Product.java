package com.desafio.hotmart.product;

import com.desafio.hotmart.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private User user;

    private BigDecimal price;

    private String code;

    @Min(value = 1) @Max(value = 12)
    private int maximumNumberOfInstallments;

    private boolean interestPaidByCustomer;

    @Deprecated
    public Product() { }

    public Product(User user, BigDecimal price, String code, int maximumNumberOfInstallments, boolean interestPaidByCustomer) {
        this.user = user;
        this.price = price;
        this.code = code;
        this.maximumNumberOfInstallments = maximumNumberOfInstallments;
        this.interestPaidByCustomer = interestPaidByCustomer;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCode() {
        return code;
    }

    public int getMaximumNumberOfInstallments() {
        return maximumNumberOfInstallments;
    }

    public boolean isInterestPaidByCustomer() {
        return interestPaidByCustomer;
    }
}