package com.przemarcz.order.mapper;


import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TextMapper {
    default UUID toUUID(CharSequence value) {
        return UUID.fromString(value.toString());
    }
    default BigDecimal toBigDecimal(Object value) {
        return new BigDecimal(value.toString());
    }
    default Integer toInteger(Object value) {
        return Integer.valueOf(value.toString());
    }
    default String toString(CharSequence value) {
        return value.toString();
    }
}
