package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.OpinionDto;
import com.przemarcz.restaurant.model.Opinion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface OpinionMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    Opinion toOpinion(OpinionDto opinionDto, UUID userId);

    @Mapping(target = "rate", source = "rate", qualifiedByName = "scaleRate2")
    OpinionDto toOpinionDto(Opinion opinion);

    @Named("scaleRate2")
    default BigDecimal setScaleBigDecimal(BigDecimal value ) {
        return isNull(value) ? null : BigDecimal.valueOf(Math.ceil(value.floatValue() * 2 - 1) / 2);
    }
}
