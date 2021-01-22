package com.przemarcz.restaurant.model;

import com.przemarcz.restaurant.exception.AlreadyExistException;
import com.przemarcz.restaurant.exception.NotFoundException;
import com.przemarcz.restaurant.model.enums.Days;
import com.przemarcz.restaurant.model.enums.RestaurantCategory;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.przemarcz.restaurant.dto.TableReservationDto.CheckReservationStatusRequest;
import static com.przemarcz.restaurant.dto.WorkTimeDto.WorkTimeRequest;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Entity
@javax.persistence.Table(name = "restaurants")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class Restaurant {

    private static final int TWO_INT = 2;
    private static final int ZERO = 0;
    private static final int SEVEN = 7;
    private static final BigDecimal TWO = new BigDecimal(2);
    private static final BigDecimal MIN = new BigDecimal("0.5");
    private static final BigDecimal MAX = new BigDecimal(5);

    @Id
    private UUID id;
    private String name;
    private String image;
    @Column(name = "owner_id")
    private UUID ownerId;
    private String city;
    private String street;
    @Column(name = "house_number")
    private String houseNumber;
    @Column(name = "phone_number")
    private String phoneNumber;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "restaurant_id")
    private List<Meal> meals = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
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
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id")
    private List<Opinion> opinions = new ArrayList<>();
    private BigDecimal rate;
    private String category;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "restaurant_id")
    private List<Table> tables = new ArrayList<>();

    public Set<RestaurantCategory> getCategory(){
        if(isNull(category)){
            return Collections.emptySet();
        }
        String[] categoriesArr = category.split(",");
        Set<RestaurantCategory> restaurantCategories = new HashSet<>();
        Arrays.stream(categoriesArr).forEach(
                cat -> restaurantCategories.add(RestaurantCategory.valueOf(cat))
        );
        return restaurantCategories;
    }

    public void setCategory(Set<RestaurantCategory> restaurantCategories) {
        category = StringUtils.join(restaurantCategories, ",");
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
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

    public void addOpinion(Opinion opinion) {
        if(isUserAddedOpinionBefore(opinion)){
            throw new AlreadyExistException(String.format("Opinion by user %s was added before!", opinion.getUserId()));
        }
        if(isRateOpinionInRange(opinion.getRate())) {
            Optional<BigDecimal> restaurantRate = Optional.ofNullable(rate);
            restaurantRate.ifPresentOrElse(
                    rateOld -> rate = rateOld.add(opinion.getRate()).divide(TWO, TWO_INT, RoundingMode.DOWN),
                    () -> rate = opinion.getRate()
            );
            if (CollectionUtils.isEmpty(opinions)) {
                opinions = new ArrayList<>();
                opinions.add(opinion);
            }
        }else{
            throw new IllegalArgumentException("Opinion is not in range!");
        }
    }

    private boolean isUserAddedOpinionBefore(Opinion opinion) {
        if (CollectionUtils.isEmpty(opinions)) {
            return false;
        }
        return opinions.stream().anyMatch(opinion1 -> opinion1.getUserId().equals(opinion.getUserId()));
    }

    private boolean isRateOpinionInRange(BigDecimal rate) {
        return rate.compareTo(MIN) >= ZERO && rate.compareTo(MAX) <= ZERO;
    }

    public void delete() {
        isDeleted = true;
    }

    public void addTables(List<Table> tables) {
        this.tables.addAll(tables);
    }

    public void addWorkTime(List<WorkTime> incomingTime) {
        List<WorkTime> finalWorksTime = new ArrayList<>();
        if (incomingTime.size() == SEVEN) {
            incomingTime.forEach(
                    workTime -> {
                        if (areAllValuesCorrect(workTime.getFrom(), workTime.getTo())) {
                            finalWorksTime.add(workTime);
                        } else {
                            throw new IllegalArgumentException("Bad time values!");
                        }
                    }
            );
        } else {
            throw new IllegalArgumentException("Not enough time values!");
        }
        worksTime=finalWorksTime;
    }

    public void updateWorkTime(List<WorkTimeRequest> timeIncoming) {
        timeIncoming.forEach(workTimeDto -> {
            WorkTime day = getCorrectDay(workTimeDto);
            LocalTime from = workTimeDto.getFrom();
            LocalTime to = workTimeDto.getTo();
            if(areAllValuesCorrect(from, to)){
                day.setFrom(workTimeDto.getFrom());
                day.setTo(workTimeDto.getTo());
            }
        });
    }

    private WorkTime getCorrectDay(WorkTimeRequest workTimeRequest) {
        return this.worksTime.stream().filter(
                workTime -> workTimeRequest.getDay().equals(workTime.getDay())
        ).findFirst().orElseThrow(() -> new IllegalArgumentException("Something gone wrong!"));
    }

    private boolean areAllValuesCorrect(LocalTime from, LocalTime to) {
        return areValuesNonNull(from,to) && isFromSmaller(from,to) || areValuesNull(from,to) && areValuesTheSame(from,to);
    }

    private boolean areValuesNull(LocalTime from, LocalTime to) {
        return isNull(from) && isNull(to);
    }

    private boolean areValuesTheSame(LocalTime from, LocalTime to) {
        return from == to;
    }

    private boolean areValuesNonNull(LocalTime from, LocalTime to) {
        return nonNull(from) && nonNull(to);
    }

    private boolean isFromSmaller(LocalTime from, LocalTime to) {
        return from.isBefore(to);
    }

    public void checkReservationTime(CheckReservationStatusRequest reservationRequest) {
        worksTime.forEach(workTime -> {
            if (workTime.isDayEquals(reservationRequest.getDay().getDayOfWeek().getValue()) && !workTime.isTimeInRange(reservationRequest.getFrom(), reservationRequest.getTo())) {
                throw new NotFoundException("No available hours found!");
            }
        });
    }

    public void addReservation(UUID tableId, Reservation reservation) {
        tables.stream().filter(table -> table.getId().equals(tableId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Table not found!"))
                .addReservation(reservation);
    }

    public List<LocalTime> getWorkTimeOfDay() {
        Days day = Days.valueOf(LocalDate.now().getDayOfWeek().getValue()).orElseThrow(IllegalArgumentException::new);
        WorkTime workTime = worksTime.stream().filter(time -> time.getDay().equals(day)).findFirst().orElseThrow(IllegalArgumentException::new);
        return Arrays.asList(workTime.getFrom(), workTime.getTo());
    }
}
