package com.przemarcz.auth.controller;

import com.przemarcz.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static com.przemarcz.auth.domain.dto.UserDto.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser(Principal user) {
        return new ResponseEntity<>(userService.getUser(user.getName()), HttpStatus.OK);
    }

    @PostMapping("/active")
    public ResponseEntity<Void> activeAccount(@Valid @RequestBody ActivationUserRequest userActivation) {
        userService.active(userActivation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest userRequest, Principal user) {
        return new ResponseEntity<>(userService.updateUser(userRequest, user.getName()), HttpStatus.OK);
    }
}
