package com.przemarcz.restaurant.mapper;


import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TextMapper {
    default UUID toUUID(CharSequence value) {
        return UUID.fromString(value.toString());
    }

    default CharSequence toCharSequence(UUID value) {
        return value.toString();
    }

    default CharSequence toCharSequence(String value) {
        return value;
    }
}
