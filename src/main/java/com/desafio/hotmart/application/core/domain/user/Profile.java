package com.desafio.hotmart.application.core.domain.user;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Embeddable
public class Profile {

    private String firstName;

    private String name;

    @NotBlank
    private String username;

    @Deprecated
    public Profile() { }

    public Profile(String firstName, String name, String username) {
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
        Profile profile = (Profile) o;
        return Objects.equals(firstName, profile.firstName) && Objects.equals(name, profile.name) && Objects.equals(username, profile.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, name, username);
    }
}
