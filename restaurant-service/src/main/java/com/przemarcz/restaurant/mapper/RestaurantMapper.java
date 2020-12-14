package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.WorkTimeDto.WorkTimeResponse;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.model.WorkTime;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.przemarcz.restaurant.dto.RestaurantDto.*;
import static com.przemarcz.restaurant.dto.WorkTimeDto.WorkTimeRequest;
import static java.util.Objects.isNull;

@Mapper(componentModel = "spring", uses = MealMapper.class, imports = BigDecimal.class)
public interface RestaurantMapper {

    @Mapping(target = "paymentOnline", expression = "java(false)")
    @Mapping(target = "worksTime", ignore = true)
    Restaurant toRestaurant(CreateRestaurantRequest createRestaurantRequest, UUID ownerId);

    @Mapping(target = "worksTime", ignore = true)
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRestaurant(@MappingTarget Restaurant restaurant, UpdateRestaurantRequest updateRestaurantRequest);

    @Mapping(target = "rate", source = "rate", qualifiedByName = "scaleRate")
    RestaurantResponse toRestaurantResponse(Restaurant restaurant);

    @Mapping(target = "rate", source = "rate", qualifiedByName = "scaleRate")
    AllRestaurantResponse toAllRestaurantResponse(Restaurant restaurant);

    @Mapping(target = "rate", source = "rate", qualifiedByName = "scaleRate")
    AllRestaurantOwnerResponse toAllRestaurantOwnerResponse(Restaurant restaurant);

    @Mapping(target = "rate", source = "rate", qualifiedByName = "scaleRate")
    RestaurantOwnerResponse toRestaurantOwnerResponse(Restaurant restaurant);

    List<WorkTimeResponse> toWorkTimeResponse(List<WorkTime> workTime);
    List<WorkTime> toWorkTime(List<WorkTimeRequest> workTime);

    @Named("scaleRate")
    default BigDecimal setScaleBigDecimal(BigDecimal value ) {
        return isNull(value) ? null : BigDecimal.valueOf(Math.ceil(value.floatValue() * 2 - 1) / 2);
    }
}
