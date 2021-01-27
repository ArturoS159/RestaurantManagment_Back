package com.przemarcz.order.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
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
    private UUID id = UUID.randomUUID();
    private String name;
    private BigDecimal price;
    private String image;
    private String ingredients;
    private BigDecimal timeToDo;
    private Integer quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return id.equals(meal.id) && name.equals(meal.name) && price.equals(meal.price) && image.equals(meal.image) && ingredients.equals(meal.ingredients) && timeToDo.equals(meal.timeToDo) && quantity.equals(meal.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, image, ingredients, timeToDo, quantity);
    }
}
