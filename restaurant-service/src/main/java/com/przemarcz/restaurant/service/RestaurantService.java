package com.przemarcz.restaurant.service;

import com.przemarcz.avro.AccessAvro;
import com.przemarcz.avro.AddDelete;
import com.przemarcz.restaurant.dto.RestaurantDto;
import com.przemarcz.restaurant.exception.NotFoundException;
import com.przemarcz.restaurant.mapper.RestaurantMapper;
import com.przemarcz.restaurant.mapper.TextMapper;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.model.WorkTime;
import com.przemarcz.restaurant.repository.RestaurantRepository;
import com.przemarcz.restaurant.specification.RestaurantSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final TextMapper textMapper;
    private final KafkaTemplate<String, AccessAvro> accessKafkaTemplate;

    @Value("${spring.kafka.topic-orders}")
    private String topicOrders;
    @Value("${spring.kafka.topic-access-owner}")
    private String topicAccess;

    @Transactional(value = "transactionManager", readOnly = true)
    public Page<RestaurantDto> getAllRestaurants(Pageable pageable, RestaurantDto restaurantDto) {
        RestaurantSpecification specification = new RestaurantSpecification(restaurantDto);
        Page<Restaurant> restaurants = restaurantRepository.findAll(specification, pageable);
        return restaurants.map(restaurantMapper::toRestaurantPublicDto);
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public Page<RestaurantDto> getAllRestaurantForOwner(String ownerId, Pageable pageable) {
        return restaurantRepository.findAllByOwnerId(textMapper.toUUID(ownerId), pageable).map(restaurantMapper::toRestaurantDto);
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public RestaurantDto getRestaurant(UUID restaurantId) {
        return restaurantMapper.toRestaurantDto(
                getRestaurantFromDatabase(restaurantId)
        );
    }

    @Transactional("chainedKafkaTransactionManager")
    public RestaurantDto addRestaurant(String userId, RestaurantDto restaurantDto) {
        Restaurant restaurant = restaurantMapper.toRestaurant(restaurantDto, textMapper.toUUID(userId));
        List<WorkTime> worksTime = restaurantMapper.toWorkTime(restaurantDto.getWorksTime());
        restaurant.setWorkTime(worksTime);
        restaurantRepository.save(restaurant);
        sendMessageAddRestaurant(userId, restaurant);
        return restaurantMapper.toRestaurantDto(restaurant);
    }

    private void sendMessageAddRestaurant(String userId, Restaurant restaurant) {
        AccessAvro accessAvro = new AccessAvro(AddDelete.ADD, restaurant.getId().toString(), userId);
        accessKafkaTemplate.send(topicAccess, accessAvro);
    }

    public RestaurantDto updateRestaurant(UUID restaurantId, RestaurantDto restaurantDto) {
        Restaurant restaurant = getRestaurantFromDatabase(restaurantId);
        restaurantMapper.updateRestaurant(restaurant,restaurantDto);
        restaurant.updateWorkTime(restaurantDto.getWorksTime());
        restaurantRepository.save(restaurant);
        return restaurantMapper.toRestaurantDto(restaurant);
    }

    public void delRestaurant(UUID restaurantId) {
        //TODO
    }

    public Restaurant getRestaurantFromDatabase(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(String.format("Restaurant %s not found!", restaurantId)));
    }
}
