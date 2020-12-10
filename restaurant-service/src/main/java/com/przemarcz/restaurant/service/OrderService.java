package com.przemarcz.restaurant.service;

import com.przemarcz.avro.*;
import com.przemarcz.restaurant.dto.OrderDto;
import com.przemarcz.restaurant.exception.NotFoundException;
import com.przemarcz.restaurant.mapper.AvroMapper;
import com.przemarcz.restaurant.mapper.TextMapper;
import com.przemarcz.restaurant.model.Meal;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public void orderMeals(UUID restaurantId, OrderDto orderDto, String userId) {
        if(orderDto.getPaymentMethod()== PaymentMethod.ONLINE&&!isPaymentAvailable(restaurantId)){
            throw new NotFoundException("Payment not found!");
        }
        if(orderDto.getOrderType()== OrderType.IN_LOCAL){
            throw new IllegalArgumentException("Only restaurant staff may order local!");
        }
        OrderAvro orderAvro = avroMapper.toOrderAvro(orderDto, restaurantId, userId);
        List<MealAvro> meals = getMealsFromDatabase(restaurantId, orderDto);
        orderAvro.setMeals(meals);
        sendMessageOrder(orderAvro);
    }

    private List<MealAvro> getMealsFromDatabase(UUID restaurantId, OrderDto orderDto) {
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

    @Transactional("chainedKafkaTransactionManager")
    public void orderMealsByStaff(UUID restaurantId, OrderDto orderDto) {
        OrderAvro orderAvro = avroMapper.toOrderAvro(orderDto, restaurantId, null);
        List<MealAvro> meals = getMealsFromDatabase(restaurantId, orderDto);
        orderAvro.setMeals(meals);
        sendMessageOrder(orderAvro);
    }

    public void addOrDeletePayment(PaymentAvro paymentAvro) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(textMapper.toUUID(paymentAvro.getRestaurantId()));
        restaurant.setPaymentOnline(AddDelete.ADD == paymentAvro.getType());
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public boolean isPaymentAvailable(UUID restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        return restaurant.isPaymentOnline();
    }

}
