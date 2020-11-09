package com.przemarcz.order.mapper;

import com.przemarcz.avro.OrderAvro;
import com.przemarcz.order.dto.OrderDto;
import com.przemarcz.order.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TextMapper.class)
public interface OrderMapper {
    Order toOrder(OrderAvro orderAvro);

    OrderDto toOrderDto(Order order);
}
