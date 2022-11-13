package com.authentication.Service;

import com.authentication.Model.User;
import com.authentication.Repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private static Logger logger = LogManager.getLogger(UserService.class.getName());

    public void updateEmail(String token, String email) {
        Optional<User> user = userRepository.getUser(email);
        if (user.isPresent()) {
            user.get().setEmail(email);
            userRepository.save(user.get());
        }
    }

    public void updateName(String token, String name) {

        Optional<User> user = userRepository.getUser(name);
        if (user.isPresent()) {
            user.get().setName(name);
            userRepository.save(user.get());
        }
    }

    public void userUpdatePassword(String token, String password) {
        Optional<User> user = userRepository.getUser(password);
        if (user.isPresent()) {
            user.get().setPassword(password);
            userRepository.save(user.get());
        }
    }

    public void deleteUser(String token) {
        Optional<User> user = userRepository.getUser(token);
        if (user.isPresent()) {
            logger.info("User Account deleted successfully");
        } else {
            logger.warn("Can't delete user.");
        }
    }
}
