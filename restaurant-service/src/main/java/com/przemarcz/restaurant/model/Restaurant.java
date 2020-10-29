package com.przemarcz.restaurant.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    private String name;
    private String image;
    @ElementCollection(targetClass = RestaurantCategory.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "restaurant_category")
    private Set<RestaurantCategory> category;
    @Column(name = "owner_id")
    private UUID ownerId;
    private String city;
    private String street;
    @Column(name = "house_number")
    private String houseNumber;
    @Column(name = "phone_number")
    private String phoneNumber;
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "restaurant_id")
    private List<Meal> meals;

    public void addMeal(Meal meal) {
        meals.add(meal);
    }
}
