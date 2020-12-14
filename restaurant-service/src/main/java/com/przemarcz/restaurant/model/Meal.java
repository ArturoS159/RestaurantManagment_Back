package com.przemarcz.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "meals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meal {
    @Id
    private UUID id = UUID.randomUUID();
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
