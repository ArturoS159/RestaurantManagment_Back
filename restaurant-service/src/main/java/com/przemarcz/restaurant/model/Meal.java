package com.przemarcz.restaurant.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "meals")
@Getter
@Setter
@NoArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(name = "restaurant_id")
    private UUID restaurantId;
    private String name;
    private BigDecimal price;
    private String image;
    private String ingredients;
    @Column(name = "time_to_do")
    private BigDecimal timeToDo;
}
