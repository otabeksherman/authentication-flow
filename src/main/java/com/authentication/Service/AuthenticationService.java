package com.authentication.Service;

import com.authentication.Model.User;
import com.authentication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    private Map<String, String> tokens = new HashMap<>();

    @PostConstruct
    public void init() {
        userRepository.initUsers();
    }

    public void createUser(String email, String name, String password) {
        if (!userRepository.getUser(email).isPresent()) {
            User user = new User(email, name, password);
            userRepository.save(user);
        }
    }

    public Optional<String> logIn(String email, String password) {
        Optional<User> user = userRepository.getUser(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            if (!tokens.containsValue(email)) {
                String token = generateToken(email);
                tokens.put(email, token);
                return Optional.of(token);
            } else {
                return Optional.of(tokens.get(email));
            }
        } else {
            return Optional.empty();
        }
    }

    public boolean loggedIn(String token) {
        return tokens.containsValue(token);
    }

    private String generateToken(String email) {
        return email + ThreadLocalRandom.current().nextInt(10000, 99999);
    }
}
