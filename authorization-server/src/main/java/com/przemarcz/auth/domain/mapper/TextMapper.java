package com.przemarcz.auth.domain.mapper;

import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TextMapper {
    default UUID toUUID(CharSequence value) {
        return UUID.fromString(value.toString());
    }
}
