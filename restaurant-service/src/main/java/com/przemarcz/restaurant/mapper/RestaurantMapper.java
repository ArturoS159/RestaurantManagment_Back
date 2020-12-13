package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.RestaurantDto;
import com.przemarcz.restaurant.dto.WorkTimeDto;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.model.WorkTime;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.przemarcz.restaurant.dto.RestaurantDto.*;
import static java.util.Objects.isNull;

@Mapper(componentModel = "spring", uses = MealMapper.class, imports = BigDecimal.class)
public interface RestaurantMapper {

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "meals", ignore = true)
//    @Mapping(target = "paymentOnline", expression = "java(false)")
//    @Mapping(target = "worksTime", ignore = true)
//    Restaurant toRestaurant(RestaurantDto restaurantDto, UUID ownerId);
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "ownerId", ignore = true)
//    @Mapping(target = "worksTime", ignore = true)
//    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
//            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateRestaurant(@MappingTarget Restaurant restaurant, RestaurantDto restaurantDto);

//    @Mapping(target = "rate", source = "rate", qualifiedByName = "scaleRate")
//    RestaurantDto toRestaurantDtoForOwner(Restaurant restaurant);

//    @Mapping(target = "nip", ignore = true)
//    @Mapping(target = "regon", ignore = true)
//    @Mapping(target = "open", ignore = true)
//    @Mapping(target = "rate", source = "rate", qualifiedByName = "scaleRate")
//    @Mapping(target = "meals", ignore= true) //ignore
//    RestaurantDto toRestaurantPublicDto(Restaurant restaurant);

    @Mapping(target = "rate", source = "rate", qualifiedByName = "scaleRate")
    AllRestaurantResponse toAllRestaurantPublic(Restaurant restaurant);

    List<WorkTime> toWorkTime(List<WorkTimeDto> workTimeDto);

    @Named("scaleRate")
    default BigDecimal setScaleBigDecimal(BigDecimal value ) {
        return isNull(value) ? null : BigDecimal.valueOf(Math.ceil(value.floatValue() * 2 - 1) / 2);
    }
}
