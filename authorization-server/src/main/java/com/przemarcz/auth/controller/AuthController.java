package com.przemarcz.auth.controller;

import com.przemarcz.auth.dto.RegisterPersonalData;
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
    public ResponseEntity<Void> register(@RequestBody RegisterUser registerUser) {
        authService.register(registerUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/register/{login}/personal-data")
    public ResponseEntity<Void> continueRegister(@RequestBody RegisterPersonalData registerPersonalData,
                                                 @PathVariable String login) {
        authService.continueRegister(registerPersonalData,login);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
