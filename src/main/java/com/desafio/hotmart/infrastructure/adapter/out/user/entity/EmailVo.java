package com.desafio.hotmart.infrastructure.adapter.out.user.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Embeddable
public class EmailVo {

    @jakarta.validation.constraints.Email
    @NotBlank
    private String email;

    @Deprecated
    public EmailVo() { }

    public EmailVo(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmailVo emailVO = (EmailVo) o;
        return Objects.equals(email, emailVO.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
