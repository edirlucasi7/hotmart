package com.hotmart.infrastructure.adapter.in.user;

import com.hotmart.application.core.domain.user.User;

import java.util.Optional;

public interface UserServicePort {

    Optional<User> findByEmail(String email);

    User getBy(String email);
}
