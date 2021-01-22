package com.przemarcz.restaurant.model

import com.przemarcz.restaurant.exception.AlreadyExistException
import com.przemarcz.restaurant.model.enums.Days
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalTime

class RestaurantTest extends Specification {

    def "should return correct meal by id"() {
        given:
        UUID mealId = UUID.randomUUID()
        List<Meal> meals = new ArrayList<>()
        meals << Meal.builder()
                .id(mealId)
                .name("Meal1")
                .build()
        meals << Meal.builder()
                .id(UUID.randomUUID())
                .name("Meal2")
                .build()
        Restaurant restaurant = Restaurant.builder().build()
        restaurant.setMeals(meals)
        when:
        Meal meal = restaurant.getMeal(mealId)
        then:
        meal.name == "Meal1"
    }

    def "should delete correct meal by id"() {
        given:
        UUID mealId = UUID.randomUUID()
        List<Meal> meals = new ArrayList<>()
        meals << Meal.builder()
                .id(mealId)
                .name("Meal1")
                .build()
        meals << Meal.builder()
                .id(UUID.randomUUID())
                .name("Meal2")
                .build()
        Restaurant restaurant = Restaurant.builder().build()
        restaurant.setMeals(meals)
        when:
        restaurant.deleteMeal(mealId)
        then:
        restaurant.meals.size() == 1
        restaurant.meals.get(0).name == "Meal2"
    }

    def "should add opinon to restuarant when all values are correct"() {
        given:
        Opinion opinion = Opinion.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .rate(BigDecimal.valueOf(3))
                .build()
        Restaurant restaurant = Restaurant.builder().build()
        when:
        restaurant.addOpinion(opinion)
        then:
        restaurant.opinions.size() == 1
    }

    def "should add opinon to restuarant when user added opinion before"() {
        given:
        UUID userId = UUID.randomUUID()
        Opinion opinion1 = Opinion.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .rate(BigDecimal.valueOf(3))
                .build()
        Opinion opinion2 = Opinion.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .rate(BigDecimal.valueOf(3))
                .build()
        Restaurant restaurant = Restaurant.builder().build()
        restaurant.addOpinion(opinion1)
        when:
        restaurant.addOpinion(opinion2)
        then:
        thrown AlreadyExistException
    }

    @Unroll
    def "should not add opinon to restuarant when value are not in range"() {
        given:
        Opinion opinion = Opinion.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .rate(BigDecimal.valueOf(rate as double))
                .build()
        Restaurant restaurant = Restaurant.builder().build()
        when:
        restaurant.addOpinion(opinion)
        then:
        thrown returnedValue
        where:
        rate || returnedValue
        5.1  || IllegalArgumentException
        0    || IllegalArgumentException
    }


    def "should calculate rate restaurant when opinion is added first time"() {
        given:
        Opinion opinion = Opinion.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .rate(BigDecimal.valueOf(2.5))
                .build()
        Restaurant restaurant = Restaurant.builder().build()
        when:
        restaurant.addOpinion(opinion)
        then:
        restaurant.rate == 2.5
    }

    def "should calculate rate restaurant when opinion is added secound or more time"() {
        given:
        Opinion opinion1 = Opinion.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .rate(BigDecimal.valueOf(2.5))
                .build()
        Opinion opinion2 = Opinion.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .rate(BigDecimal.valueOf(4))
                .build()
        Restaurant restaurant = Restaurant.builder().build()
        restaurant.addOpinion(opinion1)
        when:
        restaurant.addOpinion(opinion2)
        then:
        restaurant.rate == 3.25
    }

    def "should add work time to restaurant"() {
        given:
        List<WorkTime> worksTime = new ArrayList<>()
        worksTime << WorkTime.builder()
                .day(Days.MONDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.TUESDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.WEDNESDAY)
                .from(null)
                .to(null)
                .build()
        worksTime << WorkTime.builder()
                .day(Days.THURSDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.FRIDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.SATURDAY)
                .from(null)
                .to(null)
                .build()
        worksTime << WorkTime.builder()
                .day(Days.SUNDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        Restaurant restaurant = Restaurant.builder().build()
        when:
        restaurant.addWorkTime(worksTime)
        then:
        restaurant.worksTime.size()==7
    }

    def "should not add work time to restaurant when size of works time not equals 7"() {
        given:
        List<WorkTime> worksTime = new ArrayList<>()
        worksTime << WorkTime.builder()
                .day(Days.MONDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        Restaurant restaurant = Restaurant.builder().build()
        when:
        restaurant.addWorkTime(worksTime)
        then:
        thrown IllegalArgumentException
    }

    def "should not add work time to restaurant when one of days from is after then to"() {
        given:
        List<WorkTime> worksTime = new ArrayList<>()
        worksTime << WorkTime.builder()
                .day(Days.MONDAY)
                .from(LocalTime.parse("22:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.TUESDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.WEDNESDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.THURSDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.FRIDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.SATURDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.SUNDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        Restaurant restaurant = Restaurant.builder().build()
        when:
        restaurant.addWorkTime(worksTime)
        then:
        thrown IllegalArgumentException
    }

    def "should not add work time to restaurant when one of days one filed is null value"() {
        given:
        List<WorkTime> worksTime = new ArrayList<>()
        worksTime << WorkTime.builder()
                .day(Days.MONDAY)
                .from(null)
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.TUESDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.WEDNESDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.THURSDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.FRIDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.SATURDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        worksTime << WorkTime.builder()
                .day(Days.SUNDAY)
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        Restaurant restaurant = Restaurant.builder().build()
        when:
        restaurant.addWorkTime(worksTime)
        then:
        thrown IllegalArgumentException
    }

}
