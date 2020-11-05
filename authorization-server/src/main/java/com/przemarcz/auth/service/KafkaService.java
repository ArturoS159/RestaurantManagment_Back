package com.przemarcz.auth.service;

import com.przemarcz.auth.exception.NotFoundException;
import com.przemarcz.auth.model.User;
import com.przemarcz.auth.model.enums.Role;
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
        final UUID userId = charSequenceToUuid(accessAvro.value().getUserId());
        final UUID restaurantId = charSequenceToUuid(accessAvro.value().getRestaurantId());
        //TODO is exist&&refactors
        User user = getUserFromDatabase(userId);
        if (isAddedRestaurant(accessAvro)) {
            user.addRole(Role.OWNER, restaurantId);
        } else {
            user.delRole(Role.OWNER, restaurantId);
        }
        userRepository.save(user);
        log.info(String.format("Owner role update to user: %s", userId));
    }

    private boolean isAddedRestaurant(ConsumerRecord<String, AccesAvro> accessAvro) {
        return accessAvro.value().getType().equals(RestaurantDo.ADD);
    }

    private User getUserFromDatabase(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User %s not found!", userId)));
    }

    private UUID charSequenceToUuid(CharSequence charSequence) {
        return UUID.fromString(charSequence.toString());
    }

}
