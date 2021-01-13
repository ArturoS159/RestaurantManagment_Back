package com.przemarcz.restaurant.service;

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
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.przemarcz.restaurant.dto.OpinionDto.CreateOpinionRequest;
import static com.przemarcz.restaurant.dto.OpinionDto.OpinionResponse;

@Service
@RequiredArgsConstructor
public class OpinionService {

    private final OpinionRepository opinionRepository;
    private final RestaurantRepository restaurantRepository;
    private final OpinionMapper opinionMapper;
    private final TextMapper textMapper;
    private final RestaurantService restaurantService;

    @Transactional(value = "transactionManager", readOnly = true)
    public Page<OpinionResponse> getAllRestaurantOpinions(UUID restaurantId, Pageable pageable) {
        return opinionRepository.findAllByRestaurantId(restaurantId,pageable).map(opinionMapper::toOpinionResponse);
    }

    @Transactional(value = "transactionManager")
    public OpinionResponse addRestaurantOpinion(CreateOpinionRequest opinionRequest, UUID restaurantId, String userId) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        Opinion opinion = opinionMapper.toOpinion(opinionRequest, textMapper.toUUID(userId));
        restaurant.addOpinion(opinion);
        restaurantRepository.save(restaurant);
        return opinionMapper.toOpinionResponse(opinion);
    }
}
