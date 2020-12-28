package com.przemarcz.restaurant.service;

import com.przemarcz.avro.*;
import com.przemarcz.restaurant.exception.NotFoundException;
import com.przemarcz.restaurant.mapper.AvroMapper;
import com.przemarcz.restaurant.mapper.TextMapper;
import com.przemarcz.restaurant.model.Meal;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.przemarcz.restaurant.dto.MealDto.OrderMealRequest;
import static com.przemarcz.restaurant.dto.OrderDto.CreateOrderPersonalRequest;
import static com.przemarcz.restaurant.dto.OrderDto.CreateOrderUserRequest;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MealRepository mealRepository;
    private final AvroMapper avroMapper;
    private final TextMapper textMapper;
    private final KafkaTemplate<String, OrderAvro> orderKafkaTemplate;
    private final RestaurantService restaurantService;

    @Value("${spring.kafka.topic-orders}")
    private String topicOrders;

    @Transactional("chainedKafkaTransactionManager")
    public void orderMealsByClient(UUID restaurantId, CreateOrderUserRequest order, String userId) {
        if(order.getPaymentMethod()== PaymentMethod.ONLINE&&!isPaymentAvailable(restaurantId)){
            throw new NotFoundException("Restaurant payment not found!");
        }
        if(order.getOrderType()== OrderType.IN_LOCAL){
            throw new IllegalArgumentException("Only restaurant staff may order local!");
        }
        OrderAvro orderAvro = avroMapper.toOrderByUser(order, restaurantId, userId);
        List<MealAvro> meals = getMealsFromDatabase(restaurantId, order.getMeals());
        orderAvro.setMeals(meals);
        sendMessageOrder(orderAvro);
    }

    private boolean isPaymentAvailable(UUID restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        return restaurant.isPaymentOnline();
    }

    @Transactional("chainedKafkaTransactionManager")
    public void orderMealsByPersonal(UUID restaurantId, CreateOrderPersonalRequest order) {
        OrderAvro orderAvro = avroMapper.toOrderByPersonal(order, restaurantId);
        List<MealAvro> meals = getMealsFromDatabase(restaurantId, order.getMeals());
        orderAvro.setMeals(meals);
        sendMessageOrder(orderAvro);
    }

    private List<MealAvro> getMealsFromDatabase(UUID restaurantId, List<OrderMealRequest> orderMealRequests) {
        List<Meal> meals = restaurantService.getRestaurantFromDatabase(restaurantId).getMeals();
        List<MealAvro> mealsAvro = new ArrayList<>();

        for(OrderMealRequest mealRequest : orderMealRequests){
            for(Meal meal : meals){
                if(mealRequest.getId().equals(meal.getId())){
                    mealsAvro.add(avroMapper.toMealAvro(mealRequest,meal));
                }
            }
        }

        orderMealRequests
                .stream()
                .filter(mealRequest -> meals.stream().anyMatch(meal -> meal.getId().equals(mealRequest.getId())))
                .map(OrderMealRequest::getQuantity);

        return orderMealRequests.stream()
                .map(mealDto -> {
                    Meal meal = mealRepository.findByIdAndRestaurantId(mealDto.getId(),restaurantId)
                            .orElseThrow(() -> new NotFoundException(String.format("Meal %s not found!", mealDto.getId())));
                    return avroMapper.toMealAvro(mealDto,meal);
                }).collect(Collectors.toList());
    }

    private void sendMessageOrder(OrderAvro order) {
        orderKafkaTemplate.send(topicOrders, order);
    }

    public void addOrDeletePayment(PaymentAvro paymentAvro) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(textMapper.toUUID(paymentAvro.getRestaurantId()));
        restaurant.setPaymentOnline(AddDelete.ADD == paymentAvro.getType());
    }

}
