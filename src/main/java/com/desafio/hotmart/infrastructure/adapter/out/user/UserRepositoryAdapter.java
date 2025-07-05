package com.desafio.hotmart.infrastructure.adapter.out.user;

import com.desafio.hotmart.application.core.domain.user.User;
import com.desafio.hotmart.application.port.user.UserRepositoryPort;
import com.desafio.hotmart.infrastructure.adapter.out.user.entity.UserEntity;
import com.desafio.hotmart.infrastructure.adapter.out.user.repository.UserEntityRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserEntityRepository userEntityRepository;

    public UserRepositoryAdapter(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public Optional<User> findByEmail_Email(String email) {
        return userEntityRepository.findByEmail_Email(email).map(UserEntity::toUser);
    }

    @Override
    public User save(User user) {
        return userEntityRepository.save(new UserEntity(user)).toUser();
    }
}
