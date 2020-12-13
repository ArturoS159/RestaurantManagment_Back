package com.przemarcz.auth.controller;

import com.przemarcz.auth.dto.UserDto;
import com.przemarcz.auth.dto.UserDto.ActivationUserRequest;
import com.przemarcz.auth.dto.UserDto.UserResponse;
import com.przemarcz.auth.service.AccessConsumerService;
import com.przemarcz.auth.service.AuthService;
import com.przemarcz.avro.AccessAvro;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private static final String TOPIC_OWNER = "access-owner";
    private final AuthService authService;
    private final AccessConsumerService accessConsumerService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser(Principal user) {
        return new ResponseEntity<>(authService.getUser(user.getName()), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserDto.RegisterUserRequest user) throws EmailException {
        authService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserDto.UpdateUserRequest userRequest, Principal user) {
        return new ResponseEntity<>(authService.updateUser(userRequest, user.getName()), HttpStatus.OK);
    }

    @PostMapping("/active")
    public ResponseEntity<Void> activeAccount(@RequestBody ActivationUserRequest userActivation) {
        authService.active(userActivation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @KafkaListener(topics = TOPIC_OWNER)
    public void consumeFromOwnerTopic(ConsumerRecord<String, AccessAvro> accessAvro) {
        accessConsumerService.addOrDeleteOwnerRole(accessAvro.value());
    }
}
