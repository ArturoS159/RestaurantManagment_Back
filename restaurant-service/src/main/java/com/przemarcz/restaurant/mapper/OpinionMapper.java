package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.OpinionDto;
import com.przemarcz.restaurant.model.Opinion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface OpinionMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    Opinion toOpinion(OpinionDto opinionDto, UUID userId);

    OpinionDto toOpinionDto(Opinion opinion);
}
