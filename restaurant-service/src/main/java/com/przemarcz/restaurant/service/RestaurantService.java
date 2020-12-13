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

import static com.przemarcz.restaurant.dto.RestaurantDto.*;

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
    public Page<AllRestaurantResponse> getAllRestaurants(RestaurantFilter allRestaurantResponse, Pageable pageable) {
        RestaurantSpecification specification = new RestaurantSpecification(allRestaurantResponse);
        Page<Restaurant> restaurants = restaurantRepository.findAll(specification, pageable);
        return restaurants.map(restaurantMapper::toAllRestaurantPublic);
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public Page<AllRestaurantOwnerResponse> getAllRestaurantForOwner(RestaurantFilter restaurantFilter, String ownerId, Pageable pageable) {
        RestaurantSpecification specification = new RestaurantSpecification(textMapper.toUUID(ownerId), restaurantFilter);
//        return restaurantRepository.findAll(specification, pageable).map(restaurantMapper::toRestaurantDtoForOwner);
        return null;
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public RestaurantDto getRestaurant(UUID restaurantId) {
//        return restaurantMapper.toRestaurantPublicDto(
//                getRestaurantFromDatabase(restaurantId)
//        );
        return null;
    }

    @Transactional("chainedKafkaTransactionManager")
    public RestaurantDto addRestaurant(String userId, RestaurantDto restaurantDto) {
//        Restaurant restaurant = restaurantMapper.toRestaurant(restaurantDto, textMapper.toUUID(userId));
//        List<WorkTime> worksTime = restaurantMapper.toWorkTime(restaurantDto.getWorksTime());
//        restaurant.setWorkTime(worksTime);
//        restaurantRepository.save(restaurant);
//        sendMessageAddRestaurant(userId, restaurant);
//        return restaurantMapper.toRestaurantDtoForOwner(restaurant);
        return null;
    }

    private void sendMessageAddRestaurant(String userId, Restaurant restaurant) {
        AccessAvro accessAvro = new AccessAvro(AddDelete.ADD, restaurant.getId().toString(), userId);
        accessKafkaTemplate.send(topicAccess, accessAvro);
    }

    public RestaurantDto updateRestaurant(UUID restaurantId, RestaurantDto restaurantDto) {
//        Restaurant restaurant = getRestaurantFromDatabase(restaurantId);
//        restaurantMapper.updateRestaurant(restaurant,restaurantDto);
//        restaurant.updateWorkTime(restaurantDto.getWorksTime());
//        restaurantRepository.save(restaurant);
//        return restaurantMapper.toRestaurantDtoForOwner(restaurant);
        return null;
    }

    public void delRestaurant(UUID restaurantId) {
        Restaurant restaurant = getRestaurantFromDatabase(restaurantId);
        restaurant.delete();
        restaurantRepository.save(restaurant);
    }

    public Restaurant getRestaurantFromDatabase(UUID restaurantId) {
        return restaurantRepository.findByIdAndIsDeleted(restaurantId,false)
                .orElseThrow(() -> new NotFoundException(String.format("Restaurant %s not found!", restaurantId)));
    }
}
