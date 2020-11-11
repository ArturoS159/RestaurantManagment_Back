package com.przemarcz.auth.controller;

import com.przemarcz.auth.dto.RegisterUser;
import com.przemarcz.auth.dto.UserActivation;
import com.przemarcz.auth.dto.UserDto;
import com.przemarcz.auth.service.AuthService;
import lombok.AllArgsConstructor;
import org.apache.commons.mail.EmailException;
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
    public ResponseEntity<UserDto> getUser(Principal user) {
        return new ResponseEntity<>(authService.getUser(user.getName()), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterUser user) throws EmailException {
        authService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateUser(Principal user,@RequestBody UserDto userDto) {
        authService.updateUser(user.getName(), userDto);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @PostMapping("/active")
    public ResponseEntity<Void> activeAccount(@RequestBody UserActivation userActivation) {
        authService.active(userActivation);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
