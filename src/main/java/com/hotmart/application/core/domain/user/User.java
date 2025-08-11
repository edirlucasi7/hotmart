package com.hotmart.application.core.domain.user;

public class User {

    private Long id;

    private Profile profile;

    private Email email;

    public User() { }

    public User(Long id, Profile profile, Email email) {
        this.id = id;
        this.profile = profile;
        this.email = email;
    }

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

    public Profile getProfile() {
        return profile;
    }
}