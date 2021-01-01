package com.przemarcz.order.specification;

import com.przemarcz.order.model.Order;
import com.przemarcz.order.model.Order_;
import com.przemarcz.order.model.enums.OrderStatus;
import com.przemarcz.order.model.enums.OrderType;
import com.przemarcz.order.model.enums.PaymentMethod;
import io.micrometer.core.lang.NonNullApi;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.przemarcz.order.dto.OrderDto.OrderFilter;
import static com.przemarcz.order.model.enums.OrderStatus.IN_PROGRESS;
import static com.przemarcz.order.model.enums.PaymentMethod.ONLINE;
import static java.util.Objects.nonNull;


@NonNullApi
public class OrderSpecification implements Specification<Order> {

    private final UUID restaurantId;
    private final PaymentMethod paymentMethod;
    private final OrderType orderType;
    private final OrderStatus orderStatus;
    LocalDate fromDay;
    LocalDate toDay;
    private final BigDecimal fromPrice;
    private final BigDecimal toPrice;
    private final Boolean payed;
    private final Boolean archived;

    public OrderSpecification(UUID restaurantId, OrderFilter orderFilter) {
        this.restaurantId = restaurantId;
        this.paymentMethod = orderFilter.getPaymentMethod();
        this.orderType = orderFilter.getOrderType();
        this.orderStatus = orderFilter.getOrderStatus();
        this.fromDay = orderFilter.getFromDay();
        this.toDay = orderFilter.getToDay();
        this.fromPrice = orderFilter.getFromPrice();
        this.toPrice = orderFilter.getToPrice();
        this.payed = orderFilter.getPayed();
        this.archived = orderFilter.getArchived();
    }

    public OrderSpecification(UUID restaurantId) {
        this(restaurantId, new OrderFilter(ONLINE,null, IN_PROGRESS,null,null,null,null,false,null));
    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        if (nonNull(restaurantId)) {
            predicates.add(builder.equal(root.get(Order_.restaurantId), restaurantId));
        }
        if (nonNull(paymentMethod)) {
            predicates.add(builder.equal(root.get(Order_.paymentMethod), paymentMethod));
        }
        if (nonNull(orderType)) {
            predicates.add(builder.equal(root.get(Order_.orderType), orderType));
        }
        if (nonNull(orderStatus)) {
            predicates.add(builder.equal(root.get(Order_.orderStatus), orderStatus));
        }
        if (nonNull(fromDay)) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(Order_.time).as(LocalDate.class), fromDay));
        }
        if (nonNull(toDay)) {
            predicates.add(builder.lessThanOrEqualTo(root.get(Order_.time).as(LocalDate.class), toDay));
        }
        if (nonNull(fromPrice)) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(Order_.price), fromPrice));
        }
        if (nonNull(toPrice)) {
            predicates.add(builder.lessThanOrEqualTo(root.get(Order_.price), toPrice));
        }
        if (nonNull(payed)) {
            predicates.add(builder.equal(root.get(Order_.payed), payed));
        }
        if (nonNull(archived)) {
            predicates.add(builder.lessThan(root.get(Order_.time).as(LocalDate.class), LocalDate.now()));
        }else{
            predicates.add(builder.equal(root.get(Order_.time).as(LocalDate.class), LocalDate.now()));
        }
        Predicate[] predicatesArray = new Predicate[predicates.size()];
        Predicate[] finalPredicates = predicates.toArray(predicatesArray);
        builder.and(finalPredicates);
        return builder.and(finalPredicates);
    }
}
