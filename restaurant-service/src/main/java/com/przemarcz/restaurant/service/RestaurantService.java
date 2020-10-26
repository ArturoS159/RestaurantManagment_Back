package com.przemarcz.restaurant.service;

import com.przemarcz.avro.AccesAvro;
import com.przemarcz.avro.OrderAvro;
import com.przemarcz.avro.RestaurantDo;
import com.przemarcz.restaurant.dto.OrderDto;
import com.przemarcz.restaurant.dto.RestaurantDto;
import com.przemarcz.restaurant.dto.exception.NotFoundException;
import com.przemarcz.restaurant.mapper.RestaurantMapper;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final KafkaTemplate<String, OrderAvro> orderKafkaTemplate;
    private final KafkaTemplate<String, AccesAvro> accessKafkaTemplate;

    @Value("${spring.kafka.topic-orders}")
    private String topicOrders;
    @Value("${spring.kafka.topic-access-owner}")
    private String topicAccess;

    @Transactional(value = "transactionManager", readOnly = true)
    public Page<RestaurantDto> getAllRestaurants(String userId, Pageable pageable, boolean my) {
        if (my) {
            return restaurantRepository.findAllByOwnerId(convertUserIdStringToUUID(userId), pageable)
                    .map(restaurantMapper::toRestaurantDto);
        }
        return restaurantRepository.findAll(pageable).map(restaurantMapper::toRestaurantDto);
        //TODO refactor specification
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public RestaurantDto getRestaurant(UUID restaurantId) {
        return restaurantMapper.toRestaurantDto(
                restaurantRepository.findById(restaurantId)
                        .orElseThrow(() -> new NotFoundException("Restaurant not found!"))
        );
    }

    @Transactional("chainedKafkaTransactionManager")
    public void addRestaurant(String userId, RestaurantDto restaurantDto) {
        Restaurant restaurant = restaurantRepository.save(restaurantMapper.toRestaurant(restaurantDto, convertUserIdStringToUUID(userId)));
        sendMessageAddRestaurant(userId, restaurant);
    }

    private void sendMessageAddRestaurant(String userId, Restaurant restaurant) {
        AccesAvro accesAvro = new AccesAvro(RestaurantDo.ADD, restaurant.getId().toString(), userId);
        accessKafkaTemplate.send(topicAccess, accesAvro);
    }

    public void orderMeals(UUID restaurantId, OrderDto orderDto) {
        //TODO
    }

    private UUID convertUserIdStringToUUID(String userId) {
        return UUID.fromString(userId);
    }

}
