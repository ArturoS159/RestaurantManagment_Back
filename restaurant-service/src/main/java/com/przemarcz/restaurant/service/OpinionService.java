package com.przemarcz.restaurant.service;

import com.przemarcz.restaurant.dto.OpinionDto;
import com.przemarcz.restaurant.exception.NotFoundException;
import com.przemarcz.restaurant.mapper.OpinionMapper;
import com.przemarcz.restaurant.mapper.TextMapper;
import com.przemarcz.restaurant.model.Opinion;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.repository.OpinionRepository;
import com.przemarcz.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OpinionService {

    private final OpinionRepository opinionRepository;
    private final RestaurantRepository restaurantRepository;
    private final OpinionMapper opinionMapper;
    private final TextMapper textMapper;

    public Page<OpinionDto> getAllRestaurantOpinions(UUID restaurantId, Pageable pageable) {
        return opinionRepository.findAllByRestaurantId(restaurantId,pageable).map(opinionMapper::toOpinionDto);
    }

    public OpinionDto addRestaurantOpinion(OpinionDto opinionDto, UUID restaurantId, String userId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(String.format("Restaurant %s not found!", restaurantId)));
        Opinion opinion = opinionMapper.toOpinion(opinionDto, textMapper.toUUID(userId));
        restaurant.addOpinion(opinion);
        restaurantRepository.save(restaurant);
        return opinionDto;
    }
}
