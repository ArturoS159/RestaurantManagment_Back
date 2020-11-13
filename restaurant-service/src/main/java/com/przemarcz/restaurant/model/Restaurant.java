package com.przemarcz.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.przemarcz.restaurant.dto.WorkTimeDto;
import com.przemarcz.restaurant.exception.NotFoundException;
import com.przemarcz.restaurant.model.enums.Days;
import com.przemarcz.restaurant.model.enums.RestaurantCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @JsonIgnore
    private static final String MAX_TIME = "23:59";

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
    private List<WorkTime> worksTime = new ArrayList<>();
    @Column(name = "deleted")
    private boolean isDeleted;
    private String description;
    private String nip;
    private String regon;
    @Column(name = "post_code")
    private String postCode;

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public void setDefaultWorkTimeIfNotAdded() {
        //TODO refactor
        for(int i=0;i<Days.values().length;i++){
            int a=0;
            for(int y=0;y<worksTime.size();y++){
                if(Days.values()[i].name().equals(worksTime.get(y).getDay().name())){
                    a=1;
                }
            }
            if(a==0){
                WorkTime workTime = new WorkTime(Days.values()[i], LocalTime.MIN, LocalTime.parse(MAX_TIME));
                worksTime.add(workTime);
            }
        }
    }

    public void updateWorkTime(List<WorkTimeDto> timeDtos) {
        timeDtos.forEach(workTimeDto -> {
            WorkTime day = getCorrectDay(workTimeDto);
            LocalTime from = workTimeDto.getFrom();
            LocalTime to = workTimeDto.getTo();
            if(areValuesNonNull(from,to) && isFromSmaller(from,to) || areValuesNullAndTheSame(from,to)){
                day.setFrom(workTimeDto.getFrom());
                day.setTo(workTimeDto.getTo());
            }
        });
    }

    private boolean areValuesNullAndTheSame(LocalTime from, LocalTime to) {
        return isNull(from)==isNull(to);
    }

    private boolean areValuesNonNull(LocalTime from, LocalTime to) {
        return nonNull(from) && nonNull(to);
    }

    private boolean isFromSmaller(LocalTime from, LocalTime to) {
        return from.isBefore(to);
    }

    private WorkTime getCorrectDay(WorkTimeDto workTimeDto) {
        return this.worksTime.stream().filter(
                workTime -> workTimeDto.getDay().equals(workTime.getDay())
        ).findFirst().orElseThrow(() -> new NotFoundException("Something gone wrong!"));
    }
}
