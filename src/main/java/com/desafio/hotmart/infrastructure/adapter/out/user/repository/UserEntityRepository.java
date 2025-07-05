package com.desafio.hotmart.infrastructure.adapter.out.user.repository;

import com.desafio.hotmart.infrastructure.adapter.out.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail_Email(String email);
}
