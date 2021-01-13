package com.przemarcz.restaurant.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "meals")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class Meal {
    @Id
    private UUID id;
    @Column(name = "restaurant_id")
    private UUID restaurantId;
    private String name;
    private BigDecimal price;
    private String image;
    private String ingredients;
    @Column(name = "time_to_do")
    private BigDecimal timeToDo;
    private String category;
}
