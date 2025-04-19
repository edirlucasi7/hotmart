package com.desafio.hotmart.user;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Embeddable
public class EmailVO {

    @jakarta.validation.constraints.Email
    @NotBlank
    private String email;

    @Deprecated
    public EmailVO() { }

    public EmailVO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmailVO emailVO = (EmailVO) o;
        return Objects.equals(email, emailVO.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
