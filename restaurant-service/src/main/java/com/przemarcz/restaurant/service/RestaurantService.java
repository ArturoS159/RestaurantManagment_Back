package com.przemarcz.restaurant.service;

import com.przemarcz.avro.AccesAvro;
import com.przemarcz.avro.MealAvro;
import com.przemarcz.avro.OrderAvro;
import com.przemarcz.avro.RestaurantDo;
import com.przemarcz.restaurant.dto.OrderDto;
import com.przemarcz.restaurant.dto.RestaurantDto;
import com.przemarcz.restaurant.dto.WorkTimeDto;
import com.przemarcz.restaurant.exception.NotFoundException;
import com.przemarcz.restaurant.mapper.AvroMapper;
import com.przemarcz.restaurant.mapper.RestaurantMapper;
import com.przemarcz.restaurant.mapper.TextMapper;
import com.przemarcz.restaurant.model.Meal;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.repository.MealRepository;
import com.przemarcz.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MealRepository mealRepository;
    private final RestaurantMapper restaurantMapper;
    private final TextMapper textMapper;
    private final AvroMapper avroMapper;
    private final KafkaTemplate<String, OrderAvro> orderKafkaTemplate;
    private final KafkaTemplate<String, AccesAvro> accessKafkaTemplate;

    @Value("${spring.kafka.topic-orders}")
    private String topicOrders;
    @Value("${spring.kafka.topic-access-owner}")
    private String topicAccess;

    @Transactional(value = "transactionManager", readOnly = true)
    public Page<RestaurantDto> getAllRestaurants(String userId, Pageable pageable, boolean my) {
        if (my) {
            return restaurantRepository.findAllByOwnerId(textMapper.toUUID(userId), pageable)
                    .map(restaurantMapper::toRestaurantDto);
        }
        return restaurantRepository.findAll(pageable).map(restaurantMapper::toRestaurantDto);
        //TODO refactor specification
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public RestaurantDto getRestaurant(UUID restaurantId) {
        return restaurantMapper.toRestaurantDto(
                getRestaurantFromDatabase(restaurantId)
        );
    }

    @Transactional("chainedKafkaTransactionManager")
    public UUID addRestaurant(String userId, RestaurantDto restaurantDto) {
        Restaurant restaurant = restaurantRepository.save(
                restaurantMapper.toRestaurant(restaurantDto, textMapper.toUUID(userId)));
        restaurant.setDefaultWorkTime();
        sendMessageAddRestaurant(userId, restaurant);
        return restaurant.getId();
    }

    private void sendMessageAddRestaurant(String userId, Restaurant restaurant) {
        AccesAvro accesAvro = new AccesAvro(RestaurantDo.ADD, restaurant.getId().toString(), userId);
        accessKafkaTemplate.send(topicAccess, accesAvro);
    }

    public void updateRestaurant(UUID restaurantId, RestaurantDto restaurantDto) {
        Restaurant restaurant = getRestaurantFromDatabase(restaurantId);
        restaurantMapper.updateRestaurant(restaurant,restaurantDto);
        restaurantRepository.save(restaurant);
    }

    public void updateRestaurantTime(UUID restaurantId, List<WorkTimeDto> worksTime) {
        Restaurant restaurant = getRestaurantFromDatabase(restaurantId);
        restaurant.updateWorkTime(worksTime);
        restaurantRepository.save(restaurant);
    }


    @Transactional("chainedKafkaTransactionManager")
    public void orderMeals(UUID restaurantId, OrderDto orderDto) {
        OrderAvro orderAvro = avroMapper.toOrderAvro(orderDto, restaurantId);
        List<MealAvro> meals = getCorrectlyMealsFromDatabase(restaurantId, orderDto);
        orderAvro.setMeals(meals);
        sendMessageOrder(orderAvro);
    }

    private List<MealAvro> getCorrectlyMealsFromDatabase(UUID restaurantId, OrderDto orderDto) {
        return orderDto.getMeals().stream()
                .map(mealDto -> {
                    Meal meal = mealRepository.findByIdAndRestaurantId(mealDto.getId(),restaurantId)
                            .orElseThrow(() -> new NotFoundException(String.format("Meal %s not found!", mealDto.getId())));
                    return avroMapper.toMealAvro(mealDto,meal);
                }).collect(Collectors.toList());
    }

    private void sendMessageOrder(OrderAvro order) {
        orderKafkaTemplate.send(topicOrders, order);
    }

    public void delRestaurant(UUID restaurantId) {
        //TODO
    }

    private Restaurant getRestaurantFromDatabase(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(String.format("Restaurant %s not found!", restaurantId)));
    }
}
