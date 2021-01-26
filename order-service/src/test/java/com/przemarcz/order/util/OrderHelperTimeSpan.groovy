package com.przemarcz.order.util

import com.przemarcz.order.model.Meal
import spock.lang.Specification

class OrderHelperTimeSpan extends Specification {

    def "should count order price"() {
        given:
        List<Meal> meals = new ArrayList<>()
        meals << Meal.builder().price(BigDecimal.valueOf(10.00)).quantity(1).build()
        meals << Meal.builder().price(BigDecimal.valueOf(40.55)).quantity(2).build()
        meals << Meal.builder().price(BigDecimal.valueOf(21.12)).quantity(4).build()
        meals << Meal.builder().price(BigDecimal.valueOf(99.99)).quantity(2).build()
        meals << Meal.builder().price(BigDecimal.valueOf(25.00)).quantity(6).build()
        OrderHelper helper = new OrderHelper()
        expect:
        helper.countOrderPrice(meals) == 525.56

    }
}
