package com.przemarcz.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.przemarcz.restaurant.model.enums.Days;
import com.przemarcz.restaurant.model.enums.RestaurantCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.*;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
public class Restaurant {

    @JsonIgnore
    private final static String MAX_TIME = "23:59";

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
    private List<Meal> meals = new ArrayList<>();
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "restaurant_id")
    private List<WorkTime> workTimes = new ArrayList<>();

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public void setDefaultWorkTime() {
        Arrays.stream(Days.values()).forEach(day -> {
                    WorkTime workTime = new WorkTime(day, LocalTime.MIN, LocalTime.parse(MAX_TIME));
                    workTimes.add(workTime);
                }
        );
    }
}
