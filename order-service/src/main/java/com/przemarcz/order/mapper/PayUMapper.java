package com.przemarcz.order.mapper;

import com.przemarcz.order.model.Meal;
import com.przemarcz.order.model.Order;
import com.przemarcz.order.util.payumodels.Buyer;
import com.przemarcz.order.util.payumodels.Payment;
import com.przemarcz.order.util.payumodels.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", imports = BigDecimal.class)
public interface PayUMapper {

    @Mapping(target = "buyer", source = "order", qualifiedByName = "toBuyer")
    @Mapping(target = "customerIp", expression = "java(\"123.123.123.123\")")
    @Mapping(target = "description", expression = "java(\"Zamówienie nr: \"+order.getId().toString())")
    @Mapping(target = "currencyCode", expression = "java(\"PLN\")")
    @Mapping(target = "totalAmount", source = "order.price", qualifiedByName = "deleteDot")
    @Mapping(target = "merchantPosId", source = "posId")
    @Mapping(target = "products", source = "order.meals", qualifiedByName = "toProducts")
    @Mapping(target = "continueUrl", expression = "java(\"http://localhost:3000/my_orders/\"+order.getRestaurantId( ).toString())")
    Payment toPayment(Order order, String posId);

    @Named("toBuyer")
    @Mapping(target = "email", source = "email", defaultValue = "")
    @Mapping(target = "firstName", source = "forename", defaultValue = "")
    @Mapping(target = "lastName", source = "surname", defaultValue = "")
    Buyer toBuyer(Order order);

    @Named("toProducts")
    List<Product> toProducts(List<Meal> meals);

    @Mapping(target = "unitPrice", source = "price", qualifiedByName = "deleteDot")
    Product toProduct(Meal meal);

    @Named("deleteDot")
    default String deleteDotFromString(String price){
        return price.replace(".","");
    }
}
