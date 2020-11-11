package com.przemarcz.auth.controller;

import com.przemarcz.auth.service.AccessConsumerService;
import com.przemarcz.avro.AccessAvro;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AccessConsumerController {

    private static final String TOPIC_OWNER = "access-owner";
    private  final AccessConsumerService accessConsumerService;

    @KafkaListener(topics = TOPIC_OWNER)
    public void consumeFromOwnerTopic(ConsumerRecord<String, AccessAvro> accessAvro) {
        final CharSequence userId = accessAvro.value().getUserId();
        final CharSequence restaurantId = accessAvro.value().getRestaurantId();

        accessConsumerService.consumeFromOwnerTopic(userId,restaurantId,accessAvro.value().getType());
    }
}
