package com.hotmart.application.core.service.user;

import com.hotmart.application.core.domain.user.Email;
import com.hotmart.application.core.domain.user.Profile;
import com.hotmart.application.core.domain.user.User;
import com.hotmart.application.port.user.UserRepositoryPort;
import com.hotmart.infrastructure.adapter.in.user.UserServicePort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class UserService implements UserServicePort {

    private final UserRepositoryPort userRepositoryPort;

    public UserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepositoryPort.findByEmail_Email(email);
    }

    public User getBy(String email) {
        return userRepositoryPort.findByEmail_Email(email).orElseGet(() -> create(email));
    }

    private User create(String email) {
        String username = email.substring(0, email.indexOf("@"));
        Assert.hasText(username, "username is not blank");
        return userRepositoryPort.save(new User(new Profile(null, null, username), new Email(email)));
    }
}