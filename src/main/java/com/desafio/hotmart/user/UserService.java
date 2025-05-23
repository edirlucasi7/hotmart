package com.desafio.hotmart.user;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getBy(String email) {
        return userRepository.findByEmail_Email(email).orElseGet(() -> create(email));
    }

    private User create(String email) {
        String username = email.substring(0, email.indexOf("@"));
        Assert.hasText(username, "username is not blank");
        return userRepository.save(new User(new ProfileVO("", "", username), new EmailVO(email)));
    }
}