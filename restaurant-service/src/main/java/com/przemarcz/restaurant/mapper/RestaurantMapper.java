package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.RestaurantDto;
import com.przemarcz.restaurant.model.Restaurant;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring", uses = MealMapper.class, imports = BigDecimal.class)
public interface RestaurantMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "meals", ignore = true)
    @Mapping(target = "paymentOnline", expression = "java(false)")
    Restaurant toRestaurant(RestaurantDto restaurantDto, UUID ownerId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRestaurant(@MappingTarget Restaurant restaurant, RestaurantDto restaurantDto);

    @Mapping(target = "rate", source = "rate", qualifiedByName = "scaleRate")
    RestaurantDto toRestaurantDto(Restaurant restaurant);

    @Mapping(target = "meals", ignore = true)
    @Mapping(target = "nip", ignore = true)
    @Mapping(target = "regon", ignore = true)
    @Mapping(target = "open", ignore = true)
    @Mapping(target = "rate", source = "rate", qualifiedByName = "scaleRate")
    RestaurantDto toRestaurantPublicDto(Restaurant restaurant);

    @Named("scaleRate")
    default BigDecimal setScaleBigDecimal(BigDecimal value ) {
        return isNull(value) ? null : BigDecimal.valueOf(Math.ceil(value.floatValue() * 2 - 1) / 2);
    }
}
