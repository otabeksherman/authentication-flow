package com.authentication.Controller;

import com.authentication.Service.AuthenticationService;
import jdk.jshell.Snippet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    private static Logger logger = LogManager
            .getLogger(AuthenticationController.class.getName());

    @PostMapping("register")
    public ResponseEntity<Void> register(@RequestParam String email,
                                         @RequestParam String name,
                                         @RequestParam String password) {
        if (validateParams(email, name, password)) {
            authenticationService.createUser(email, name, password);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestParam String email,
                                      @RequestParam String password) {
        Optional<String> token = authenticationService.logIn(email, password);
        if (token.isPresent()) {
            return new ResponseEntity<String>(token.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Incorrect email or password", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean validateParams(String email, String name, String password) {
        String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return patternMatches(email, emailPattern)
                && name.length() > 0 && patternMatches(password, passwordPattern);
    }

    private boolean patternMatches(String str, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(str)
                .matches();
    }
}
