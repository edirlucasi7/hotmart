package com.desafio.hotmart.infrastructure.adapter.out.user.entity;

import com.desafio.hotmart.application.core.domain.user.Email;
import com.desafio.hotmart.application.core.domain.user.Profile;
import com.desafio.hotmart.application.core.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @NotNull
    private Profile profile;

    @Embedded
    @NotNull
    private EmailVo email;

    @Deprecated
    public UserEntity() { }

    public UserEntity(Profile profile, EmailVo email) {
        this.profile = profile;
        this.email = email;
    }

    public UserEntity(User user) {
        this.id = user.getId();
        this.profile = user.getProfile();
        this.email = new EmailVo(user.getEmail());
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

    public User toUser() {
        return new User(id, profile, new Email(email.getEmail()));
    }
}
