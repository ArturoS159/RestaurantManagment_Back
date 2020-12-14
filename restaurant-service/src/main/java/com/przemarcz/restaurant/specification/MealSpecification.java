package com.przemarcz.restaurant.specification;

import com.przemarcz.restaurant.model.Meal;
import com.przemarcz.restaurant.model.Meal_;
import io.micrometer.core.lang.NonNullApi;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.przemarcz.restaurant.dto.MealDto.MealFilter;
import static java.util.Objects.nonNull;

@NonNullApi
public class MealSpecification implements Specification<Meal> {

    private final UUID restaurantId;
    private final Set<String> categories;
    private final BigDecimal fromPrice;
    private final BigDecimal toPrice;
    private final BigDecimal fromTime;
    private final BigDecimal toTime;

    public MealSpecification(UUID restaurantId, MealFilter mealFilter) {
        this.restaurantId = restaurantId;
        this.categories = getCategories(mealFilter.getCategory());
        this.fromPrice = mealFilter.getFromPrice();
        this.toPrice = mealFilter.getToPrice();
        this.fromTime = mealFilter.getFromTime();
        this.toTime = mealFilter.getToTime();
    }

    private Set<String> getCategories(String categoriesString){
        return nonNull(categoriesString) ? Set.of(categoriesString.toLowerCase().split(",")) : null;
    }

    @Override
    public Predicate toPredicate(Root<Meal> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        List<Predicate> predicatesAnd = new ArrayList<>();
        if(!CollectionUtils.isEmpty(categories)){
            predicatesAnd.add(builder.lower(root.get(Meal_.category)).in(categories));
        }
        if(nonNull(fromPrice)){
            predicatesAnd.add(builder.greaterThanOrEqualTo(root.get(Meal_.price), fromPrice));
        }
        if(nonNull(toPrice)){
            predicatesAnd.add(builder.lessThanOrEqualTo(root.get(Meal_.price), toPrice));
        }
        if(nonNull(fromTime)){
            predicatesAnd.add(builder.greaterThanOrEqualTo(root.get(Meal_.timeToDo), fromTime));
        }
        if(nonNull(toTime)){
            predicatesAnd.add(builder.lessThanOrEqualTo(root.get(Meal_.timeToDo), toTime));
        }
        predicatesAnd.add(builder.equal(root.get(Meal_.restaurantId), restaurantId));
        Predicate[] predicatesArrayAnd = new Predicate[predicatesAnd.size()];
        Predicate[] finalPredicatesAnd = predicatesAnd.toArray(predicatesArrayAnd);
        builder.and(finalPredicatesAnd);
        return builder.and(finalPredicatesAnd);
    }
}
