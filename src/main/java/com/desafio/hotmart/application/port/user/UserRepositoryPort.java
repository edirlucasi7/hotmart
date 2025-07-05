package com.desafio.hotmart.application.port.user;

import com.desafio.hotmart.application.core.domain.user.User;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findByEmail_Email(String email);

    User save(User user);
}
