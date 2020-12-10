package com.przemarcz.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.przemarcz.restaurant.dto.WorkTimeDto;
import com.przemarcz.restaurant.exception.AlreadyExistException;
import com.przemarcz.restaurant.exception.NotFoundException;
import com.przemarcz.restaurant.model.enums.Days;
import com.przemarcz.restaurant.model.enums.RestaurantCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    private static final String MAX_TIME = "23:59";
    private static final int TWO_INT = 2;
    private static final int ZERO = 0;
    private static final BigDecimal TWO = new BigDecimal(2);
    private static final BigDecimal MIN = new BigDecimal("0.5");
    private static final BigDecimal MAX = new BigDecimal(5);

    @Id
    private UUID id = UUID.randomUUID();
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
    @Column(name = "payment_online")
    private boolean paymentOnline;
    private String description;
    private String nip;
    private String regon;
    @Column(name = "post_code")
    private String postCode;
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "restaurant_id")
    private List<Opinion> opinions;
    private BigDecimal rate;

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public void setDefaultWorkTimeIfNotAdded() {
        //TODO refactor
        for(int i=0;i<Days.values().length;i++){
            int a=0;
            for (WorkTime time : worksTime) {
                if (Days.values()[i].name().equals(time.getDay().name())) {
                    a = 1;
                    break;
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
        ).findFirst().orElseThrow(() -> new IllegalArgumentException("Something gone wrong!"));
    }

    public void addOpinion(Opinion opinion) {
        if(isUserAddedOpinionBefore(opinion)){
            throw new AlreadyExistException(String.format("Opinion by user %s was added before!", opinion.getUserId()));
        }
        if(isRateOpinionInRange(opinion.getRate())){
            if(isNull(rate)){
                rate = opinion.getRate();
            }else{
                rate = rate.add(opinion.getRate()).divide(TWO, TWO_INT, RoundingMode.DOWN);
            }
            opinions.add(opinion);
        }else{
            throw new IllegalArgumentException("Opinion is not in range!");
        }
    }

    private boolean isUserAddedOpinionBefore(Opinion opinion) {
        return opinions.stream().anyMatch(opinion1 -> opinion1.getUserId().equals(opinion.getUserId()));
    }

    private boolean isRateOpinionInRange(BigDecimal rate) {
        return rate.compareTo(MIN)>=ZERO&&rate.compareTo(MAX)<=ZERO;
    }

    public Meal getMeal(UUID mealId) {
        return getMealById(mealId);
    }

    public void deleteMeal(UUID mealId) {
        meals.remove(getMealById(mealId));
    }

    private Meal getMealById(UUID mealId) {
        return meals.stream().filter(meal -> meal.getId().equals(mealId)).findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Meal %s not found!", mealId)));
    }
}
