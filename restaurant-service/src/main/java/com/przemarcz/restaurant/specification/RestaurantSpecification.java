package com.przemarcz.restaurant.specification;

import com.przemarcz.restaurant.dto.RestaurantDto;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.model.Restaurant_;
import com.przemarcz.restaurant.model.WorkTime;
import com.przemarcz.restaurant.model.WorkTime_;
import com.przemarcz.restaurant.model.enums.Days;
import com.przemarcz.restaurant.model.enums.RestaurantCategory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.sql.Array;
import java.time.LocalTime;
import java.util.*;

import static java.util.Objects.nonNull;

public class RestaurantSpecification implements Specification<Restaurant> {

    private final String name;
    private final String city;
    private final Set<RestaurantCategory> category;
    private final boolean open;
    private final BigDecimal rate;

    public RestaurantSpecification(RestaurantDto restaurantDto) {
        name = restaurantDto.getName();
        city = restaurantDto.getCity();
        category = restaurantDto.getCategory();
        open = restaurantDto.isOpen();
        rate = restaurantDto.getRate();
    }

    @Override
    public Predicate toPredicate(Root<Restaurant> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        if(nonNull(name)){
            predicates.add(builder.like(builder.lower(root.get(Restaurant_.name)),"%"+name.toLowerCase()+"%"));
        }
        if(nonNull(city)){
            predicates.add(builder.like(builder.lower(root.get(Restaurant_.city)),"%"+city.toLowerCase()+"%"));
        }
        if(!CollectionUtils.isEmpty(category)){
            category.forEach(
                    restaurantCategory -> predicates.add(builder.like(root.get(Restaurant_.category), "%"+restaurantCategory.toString()+"%"))
            );
        }
        if(open){
            final LocalTime now = LocalTime.now();
            final Days day = Days.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)).orElseThrow(NotFoundException::new);

            Join<Restaurant, WorkTime> join = root.join(Restaurant_.worksTime);
            predicates.add(
                    builder.and(
                            builder.equal(join.get(WorkTime_.day), day),
                            builder.greaterThanOrEqualTo(join.get(WorkTime_.to), now),
                            builder.lessThanOrEqualTo(join.get(WorkTime_.from), now)
                    )
            );
        }
        if(nonNull(rate)){
            predicates.add(builder.greaterThanOrEqualTo(root.get(Restaurant_.rate), rate));
        }
        Predicate[] predicatesArray = new Predicate[predicates.size()];
        Predicate[] finalPredicates = predicates.toArray(predicatesArray);
        return builder.and(finalPredicates);
    }
}
