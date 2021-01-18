package com.przemarcz.auth.controller;

import com.przemarcz.auth.domain.dto.UserDto.RegisterUserRequest;
import com.przemarcz.auth.service.AccessConsumerService;
import com.przemarcz.auth.service.AuthService;
import com.przemarcz.avro.AccessAvro;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private static final String TOPIC_OWNER = "access-owner";
    private final AuthService authService;
    private final AccessConsumerService accessConsumerService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody RegisterUserRequest user) throws EmailException {
        authService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @KafkaListener(topics = TOPIC_OWNER)
    public void consumeFromOwnerTopic(ConsumerRecord<String, AccessAvro> accessAvro) {
        accessConsumerService.addOrDeleteOwnerRole(accessAvro.value());
    }
}
