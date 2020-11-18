package com.przemarcz.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
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
    private String email;
    private String comment;
    @Column(name = "user_id")
    private UUID userId;
    private BigDecimal price;
    @Column(name = "restaurant_id")
    private UUID restaurantId;
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "order_id")
    private List<Meal> meals;
    private boolean payed;
    @Column(name = "payu_url")
    private String payUUrl;
    @Column(name = "payu_order_id")
    private String payUOrderId;
}
