package com.authentication.Controller;

import com.authentication.Service.AuthenticationService;
import com.authentication.Service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static Logger logger = LogManager.getLogger(UserController.class.getName());

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/update/email")
    public void updateUserEmail(@RequestParam String token, @RequestParam String email){
        if (!validateEmail(email)) {
            logger.warn("Invalid Email for update");
        } else if (authenticationService.loggedIn(token)) {
            userService.updateEmail(token, email);
        }
    }
    @PostMapping("/update/name")
    public void updateUserName(@RequestParam String token, @RequestParam String name){
        if (name.isEmpty() || name.length() == 0){
            logger.warn("Invalid Name for update");
        } else if (authenticationService.loggedIn(token)) {
            userService.updateName(token, name);
        }
    }

    @PostMapping("/update/password")
    public void updateUserPassword(@RequestParam String token, @RequestParam String password){
        if(validatePassword(password)){
            logger.warn("Invalid Password for update");
        } else if (authenticationService.loggedIn(token)) {
            userService.userUpdatePassword(token, password);
        }
    }
    @PostMapping("/delete")
    public void deleteUser(@RequestParam String token){
        if (authenticationService.loggedIn(token)) {
            userService.deleteUser(token);
        }
    }

    private boolean validateEmail(String email) {
        String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return patternMatches(email, emailPattern);
    }

    private boolean validatePassword(String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return patternMatches(password, passwordPattern);
    }

    private boolean patternMatches(String str, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(str)
                .matches();
    }
}
