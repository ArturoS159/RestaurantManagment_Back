package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.WorkTimeDto.WorkTimeResponse;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.model.WorkTime;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.przemarcz.restaurant.dto.RestaurantDto.*;
import static com.przemarcz.restaurant.dto.WorkTimeDto.WorkTimeRequest;
import static java.util.Objects.isNull;

@Mapper(componentModel = "spring", uses = MealMapper.class, imports = {BigDecimal.class, UUID.class, StringUtils.class})
public interface RestaurantMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "paymentOnline", expression = "java(false)")
    @Mapping(target = "worksTime", ignore = true)
    @Mapping(target = "category", expression = "java(StringUtils.join(createRestaurantRequest.getCategory(), \",\"))")
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
    AllRestaurantForOwnerResponse toAllRestaurantOwnerResponse(Restaurant restaurant);

    @Mapping(target = "rate", source = "rate", qualifiedByName = "scaleRate")
    RestaurantForOwnerResponse toRestaurantOwnerResponse(Restaurant restaurant);

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    List<WorkTime> toWorkTime(List<WorkTimeRequest> workTime);

    List<WorkTimeResponse> toWorkTimeResponse(List<WorkTime> workTime);

    @Named("scaleRate")
    default BigDecimal setScaleBigDecimal(BigDecimal value) {
        return isNull(value) ? null : BigDecimal.valueOf(Math.ceil(value.floatValue() * 2 - 1) / 2);
    }
}
