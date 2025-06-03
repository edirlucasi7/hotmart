package com.desafio.hotmart.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @NotNull
    private Profile profile;

    @Embedded
    @NotNull
    private Email email;

    @Deprecated
    public User() { }

    public User(Profile profile, Email email) {
        this.profile = profile;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return profile.getName();
    }

    public String getUsername() {
        return profile.getUsername();
    }

    public String getEmail() {
        return email.getEmail();
    }
}