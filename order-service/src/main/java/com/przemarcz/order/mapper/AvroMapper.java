package com.przemarcz.order.mapper;

import com.przemarcz.avro.OrderAvro;
import com.przemarcz.order.model.Order;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = TextMapper.class)
public interface AvroMapper {
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    Order toOrder(OrderAvro orderAvro);
}
