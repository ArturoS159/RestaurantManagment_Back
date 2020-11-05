package com.przemarcz.auth.controller;

import com.przemarcz.auth.dto.RegisterUser;
import com.przemarcz.auth.dto.UserDto;
import com.przemarcz.auth.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserData(Principal user) {
        return new ResponseEntity<>(authService.getUserData(user.getName()), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterUser user) {
        authService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/user/owner-data")
    public ResponseEntity<Void> addOwnerData(Principal user) {
        authService.addOwnerData(user.getName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
