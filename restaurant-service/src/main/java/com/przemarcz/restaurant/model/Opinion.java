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
@Table(name = "opinions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Opinion {
    @Id
    private UUID id;
    @Column(name = "restaurant_id")
    private UUID restaurantId;
    private UUID userId;
    private BigDecimal rate;
    private String description;
}
