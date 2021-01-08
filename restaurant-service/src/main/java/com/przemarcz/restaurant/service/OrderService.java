package com.przemarcz.restaurant.service;

import com.przemarcz.avro.*;
import com.przemarcz.restaurant.exception.NotFoundException;
import com.przemarcz.restaurant.mapper.AvroMapper;
import com.przemarcz.restaurant.mapper.TextMapper;
import com.przemarcz.restaurant.model.Meal;
import com.przemarcz.restaurant.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.przemarcz.restaurant.dto.MealDto.OrderMealRequest;
import static com.przemarcz.restaurant.dto.OrderDto.CreateOrderPersonalRequest;
import static com.przemarcz.restaurant.dto.OrderDto.CreateOrderUserRequest;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final AvroMapper avroMapper;
    private final TextMapper textMapper;
    private final KafkaTemplate<String, OrderAvro> orderKafkaTemplate;
    private final RestaurantService restaurantService;

    @Value("${spring.kafka.topic-orders}")
    private String topicOrders;

    @Transactional("chainedKafkaTransactionManager")
    public void orderMealsByClient(UUID restaurantId, CreateOrderUserRequest order, String userId) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);

        if(order.getPaymentMethod()== PaymentMethod.ONLINE&&!isPaymentAvailable(restaurantId)){
            throw new NotFoundException("Restaurant payment not found!");
        }
        if(order.getOrderType()== OrderType.IN_LOCAL){
            throw new IllegalArgumentException("Only restaurant staff may order local!");
        }
        OrderAvro orderAvro = avroMapper.toOrderByUser(order, restaurantId, userId, restaurant.getName());
        List<MealAvro> meals = getMealsFromDatabase(restaurant.getMeals(), order.getMeals());
        if(!meals.isEmpty()){
            orderAvro.setMeals(meals);
            sendMessageOrder(orderAvro);
        }
    }

    private boolean isPaymentAvailable(UUID restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        return restaurant.isPaymentOnline();
    }

    @Transactional("chainedKafkaTransactionManager")
    public void orderMealsByPersonal(UUID restaurantId, CreateOrderPersonalRequest order) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        OrderAvro orderAvro = avroMapper.toOrderByPersonal(order, restaurantId, restaurant.getName());
        List<MealAvro> meals = getMealsFromDatabase(restaurant.getMeals(), order.getMeals());
        if(!meals.isEmpty()){
            orderAvro.setMeals(meals);
            sendMessageOrder(orderAvro);
        }
    }

    private List<MealAvro> getMealsFromDatabase(List<Meal> meals, List<OrderMealRequest> orderMealRequests) {
        List<MealAvro> mealsAvro = new ArrayList<>();

        for(OrderMealRequest mealRequest : orderMealRequests){
            for(Meal meal : meals){
                if(mealRequest.getId().equals(meal.getId())&&mealRequest.getQuantity()!=0){
                    mealsAvro.add(avroMapper.toMealAvro(mealRequest, meal));
                }
            }
        }
        return mealsAvro;
    }

    private void sendMessageOrder(OrderAvro order) {
        orderKafkaTemplate.send(topicOrders, order);
    }

    public void addOrDeletePayment(PaymentAvro paymentAvro) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(textMapper.toUUID(paymentAvro.getRestaurantId()));
        restaurant.setPaymentOnline(AddDelete.ADD == paymentAvro.getType());
    }

}
