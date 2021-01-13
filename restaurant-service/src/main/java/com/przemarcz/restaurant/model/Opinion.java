package com.przemarcz.restaurant.model;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "opinions")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class Opinion {
    @Id
    private UUID id;
    @Column(name = "restaurant_id")
    private UUID restaurantId;
    private UUID userId;
    private BigDecimal rate;
    private String description;
}
