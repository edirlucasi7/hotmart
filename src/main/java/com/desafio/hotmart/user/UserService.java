package com.desafio.hotmart.user;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findBy(String email) {
        return userRepository.findByEmail(email).orElse(create(email));
    }

    private User create(String email) {
        String username = email.substring(0, email.indexOf("@"));
        Assert.hasText(username, "username is not blank");
        return userRepository.save(new User("", username, email));
    }
}