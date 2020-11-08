package com.przemarcz.order.helper;

import com.przemarcz.order.model.Meal;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

@Component
public class OrderHelper {

    public BigDecimal countOrderPrice(List<Meal> meals){
        Function<Meal, BigDecimal> mulPriceAndQuantity = meal ->
                meal.getPrice().multiply(BigDecimal.valueOf(meal.getQuantity()));
        return meals.stream()
                .map(mulPriceAndQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
