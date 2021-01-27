package com.przemarcz.order.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "statistics_popular")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class PopularMealsStatistic {
    @Id
    private UUID id;
    @Column(name = "meal_id")
    private UUID mealId;
    @Column(name = "meal_name")
    private String mealName;
    @Column(name = "meal_count")
    private Long count;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private TotalStatistic totalStatistic;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PopularMealsStatistic that = (PopularMealsStatistic) o;
        return mealName.equals(that.mealName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mealName);
    }

    public void increment(Integer quantity) {
        count+=quantity;
    }
}
