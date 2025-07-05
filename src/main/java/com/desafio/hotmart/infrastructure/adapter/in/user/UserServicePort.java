package com.desafio.hotmart.infrastructure.adapter.in.user;

import com.desafio.hotmart.application.core.domain.user.User;

import java.util.Optional;

public interface UserServicePort {

    Optional<User> findByEmail(String email);
}
