package com.przemarcz.order.util

import com.przemarcz.order.model.Meal
import spock.lang.Specification

class OrderHelperTest extends Specification {

    Meal prepareMeal(BigDecimal price, Integer quantity){
        Meal meal = new Meal()
        meal.price=price
        meal.quantity=quantity
        return meal
    }

    def "should count order price"() {
        given:
            List<Meal> meals = new ArrayList<>();
            meals << prepareMeal(BigDecimal.valueOf(10.00), 1)
            meals << prepareMeal(BigDecimal.valueOf(40.55), 2)
            meals << prepareMeal(BigDecimal.valueOf(21.12), 4)
            meals << prepareMeal(BigDecimal.valueOf(99.99), 2)
            meals << prepareMeal(BigDecimal.valueOf(25.00), 6)
            OrderHelper helper = new OrderHelper()
        expect:
            helper.countOrderPrice(meals) == 525.56

    }
}
