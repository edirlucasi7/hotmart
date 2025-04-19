package com.desafio.hotmart.user;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Embeddable
public class ProfileVO {

    private String firstName;

    private String name;

    @NotBlank
    private String username;

    @Deprecated
    public ProfileVO() { }

    public ProfileVO(String firstName, String name, String username) {
        this.firstName = firstName;
        this.name = name;
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProfileVO profileVO = (ProfileVO) o;
        return Objects.equals(firstName, profileVO.firstName) && Objects.equals(name, profileVO.name) && Objects.equals(username, profileVO.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, name, username);
    }
}
