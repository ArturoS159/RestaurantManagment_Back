package com.przemarcz.order.model;

import com.przemarcz.order.model.enums.OrderStatus;
import com.przemarcz.order.model.enums.OrderType;
import com.przemarcz.order.model.enums.PaymentMethod;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class Order {

    private static final int ORDER_WAITING_TIME = 60;

    @Id
    private UUID id;
    private String forename;
    private String surname;
    private String city;
    private String street;
    @Column(name = "post_code")
    private String postCode;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "house_number")
    private String houseNumber;
    private String email;
    private String comment;
    private LocalDateTime time;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_type")
    private OrderType orderType;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @Column(name = "user_id")
    private UUID userId;
    private BigDecimal price;
    @Column(name = "restaurant_id")
    private UUID restaurantId;
    @Column(name = "restaurant_name")
    private String restaurantName;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<Meal> meals;
    private boolean payed;
    @Column(name = "payu_url")
    private String payUUrl;
    @Column(name = "payu_order_id")
    private String payUOrderId;

    public boolean isOrderExpired(LocalDateTime time) {
        return time.plusMinutes(ORDER_WAITING_TIME).isBefore(time);
    }

}
