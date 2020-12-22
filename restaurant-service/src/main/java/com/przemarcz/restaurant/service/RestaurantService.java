package com.przemarcz.restaurant.service;

import com.przemarcz.avro.AccessAvro;
import com.przemarcz.avro.AddDelete;
import com.przemarcz.restaurant.exception.NotFoundException;
import com.przemarcz.restaurant.mapper.RestaurantMapper;
import com.przemarcz.restaurant.mapper.TextMapper;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.model.WorkTime;
import com.przemarcz.restaurant.repository.RestaurantRepository;
import com.przemarcz.restaurant.repository.WorkTimeRepository;
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
import static com.przemarcz.restaurant.dto.WorkTimeDto.*;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final WorkTimeRepository workTimeRepository;
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
        return restaurants.map(restaurantMapper::toAllRestaurantResponse);
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public RestaurantResponse getRestaurant(UUID restaurantId) {
        return restaurantMapper.toRestaurantResponse(
                getRestaurantFromDatabase(restaurantId)
        );
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public Page<AllRestaurantOwnerResponse> getAllRestaurantForOwner(RestaurantFilter restaurantFilter, String ownerId, Pageable pageable) {
        RestaurantSpecification specification = new RestaurantSpecification(textMapper.toUUID(ownerId), restaurantFilter);
        return restaurantRepository.findAll(specification, pageable).map(restaurantMapper::toAllRestaurantOwnerResponse);
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public RestaurantOwnerResponse getRestaurantForOwner(UUID restaurantId) {
        Restaurant restaurant = getRestaurantFromDatabase(restaurantId);
        return restaurantMapper.toRestaurantOwnerResponse(restaurant);
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public List<WorkTimeResponse> getRestaurantWorkTime(UUID restaurantId) {
        List<WorkTime> worksTime = workTimeRepository.findAllByRestaurantId(restaurantId);
        return restaurantMapper.toWorkTimeResponse(worksTime);
    }

    @Transactional("chainedKafkaTransactionManager")
    public RestaurantOwnerResponse addRestaurant(CreateRestaurantRequest createRestaurantRequest, String userId) {
        Restaurant restaurant = restaurantMapper.toRestaurant(createRestaurantRequest, textMapper.toUUID(userId));
        List<WorkTime> worksTime = restaurantMapper.toWorkTime(createRestaurantRequest.getWorksTime());
        restaurant.setWorkTime(worksTime);
        restaurantRepository.save(restaurant);
        sendMessageAccess(AddDelete.ADD, userId, restaurant.getId());
        return restaurantMapper.toRestaurantOwnerResponse(restaurant);
    }

    @Transactional(value = "transactionManager")
    public RestaurantOwnerResponse updateRestaurant(UUID restaurantId, UpdateRestaurantRequest updateRestaurantRequest) {
        Restaurant restaurant = getRestaurantFromDatabase(restaurantId);
        restaurantMapper.updateRestaurant(restaurant,updateRestaurantRequest);
        restaurant.updateWorkTime(updateRestaurantRequest.getWorksTime());
        restaurantRepository.save(restaurant);
        return restaurantMapper.toRestaurantOwnerResponse(restaurant);
    }

    @Transactional(value = "transactionManager")
    public void delRestaurant(UUID restaurantId) {
        Restaurant restaurant = getRestaurantFromDatabase(restaurantId);
        restaurant.delete();
        sendMessageAccess(AddDelete.DEL, "", restaurantId);
    }

    private void sendMessageAccess(AddDelete addDelete, String userId, UUID restaurantId) {
        AccessAvro accessAvro = new AccessAvro(addDelete, restaurantId.toString(), userId);
        accessKafkaTemplate.send(topicAccess, accessAvro);
    }



    public Restaurant getRestaurantFromDatabase(UUID restaurantId) {
        return restaurantRepository.findByIdAndIsDeleted(restaurantId,false)
                .orElseThrow(() -> new NotFoundException(String.format("Restaurant %s not found!", restaurantId)));
    }
}
