package com.przemarcz.auth.controller;

import com.przemarcz.auth.dto.UserDto;
import com.przemarcz.auth.service.AuthService;
import lombok.AllArgsConstructor;
import org.bouncycastle.asn1.ocsp.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserData(Principal user) {
        return new ResponseEntity<>(authService.getUserData(user.getName()), HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<Void> getXa(@RequestParam Date date) {
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserDto userDto) {
        authService.register(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
