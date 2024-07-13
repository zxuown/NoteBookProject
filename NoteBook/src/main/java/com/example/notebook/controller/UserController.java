package com.example.notebook.controller;

import com.example.notebook.dto.JwtUtil;
import com.example.notebook.entity.User;
import com.example.notebook.service.AuthService;
import com.example.notebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, AuthService authService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @PostMapping("/update")
    public String updateUser(@RequestHeader("Authorization") String token, @RequestBody User user) {
        token = token.substring(7);
        User currentUser = userService.loadUserByUsername(jwtUtil.getEmailFromToken(token));
        if ((userService.emailExists(user.getUsername()) &&
                !Objects.equals(currentUser.getUsername(), user.getUsername())) ||
                !Objects.equals(jwtUtil.getEmailFromToken(token),
                    userService.findById(user.getId()).getUsername())) {
            return null;
        }
        boolean bCryptPass = !currentUser.getPassword().equals(user.getPassword());
        userService.update(user, bCryptPass);
        return authService.authenticate(user.getUsername());
    }
}

