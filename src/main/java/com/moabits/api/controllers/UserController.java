package com.moabits.api.controllers;

import com.moabits.api.entities.User;
import com.moabits.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User userObject = userService.register(user);
        return ResponseEntity.ok().body(userObject);
    }
}
