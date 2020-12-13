package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.OpinionDto;
import com.przemarcz.restaurant.model.Opinion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static com.przemarcz.restaurant.dto.OpinionDto.OpinionResponse;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface OpinionMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    Opinion toOpinion(OpinionDto.CreateOpinionRequest opinionDto, UUID userId);

    OpinionResponse toOpinionResponse(Opinion opinion);
}
