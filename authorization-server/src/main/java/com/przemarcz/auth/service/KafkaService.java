package com.przemarcz.auth.service;

import com.przemarcz.auth.dto.exception.NotFoundException;
import com.przemarcz.auth.model.User;
import com.przemarcz.auth.model.enums.RoleName;
import com.przemarcz.auth.repository.UserRepository;
import com.przemarcz.avro.AccesAvro;
import com.przemarcz.avro.RestaurantDo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {
    private static final String TOPIC_OWNER = "access-owner";
    private final UserRepository userRepository;

    @KafkaListener(topics = TOPIC_OWNER)
    @Transactional("chainedKafkaTransactionManager")
    public void consumeFromOwnerTopic(ConsumerRecord<String, AccesAvro> accessAvro) {
        log.info(String.format("Owner role update to user: %s", accessAvro.value().getUserId()));
        addOrDeleteUserRole(accessAvro);
    }

    private void addOrDeleteUserRole(ConsumerRecord<String, AccesAvro> accessAvro) {
        final UUID userId = charSequenceToUuid(accessAvro.value().getUserId());
        final UUID restaurantId = charSequenceToUuid(accessAvro.value().getRestaurantId());
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User " + userId + " not found!"));
        if (accessAvro.value().getType().equals(RestaurantDo.ADD)) {
            user.addRole(RoleName.OWNER, restaurantId);
        } else {
            user.delRole(RoleName.OWNER, restaurantId);
        }
        userRepository.save(user);
    }

    private UUID charSequenceToUuid(CharSequence charSequence){
        return UUID.fromString(charSequence.toString());
    }

}
