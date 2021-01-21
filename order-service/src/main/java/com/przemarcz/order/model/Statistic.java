package com.przemarcz.order.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "statistics")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class Statistic {
    @Id
    @Column(name = "restaurant_id")
    private UUID id;
    @Column(name = "total_profit")
    private BigDecimal totalProfit;
    @Column(name = "total_orders")
    private BigDecimal totalOrders;
    @Column(name = "total_comments")
    private BigDecimal totalComments;
}
