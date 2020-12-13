package com.przemarcz.restaurant.controller;

import com.przemarcz.restaurant.service.OpinionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

import static com.przemarcz.restaurant.dto.OpinionDto.CreateOpinionRequest;
import static com.przemarcz.restaurant.dto.OpinionDto.OpinionResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class OpinionController {

    private final OpinionService opinionService;

    @GetMapping("/{restaurantId}/opinions")
    public ResponseEntity<Page<OpinionResponse>> getAllRestaurantOpinions(@PathVariable UUID restaurantId,
                                                                          Pageable pageable) {
        return new ResponseEntity<>(opinionService.getAllRestaurantOpinions(restaurantId, pageable), HttpStatus.OK);
    }

    @PostMapping("/{restaurantId}/opinions")
    public ResponseEntity<OpinionResponse> addRestaurantOpinion(@Valid @RequestBody CreateOpinionRequest opinionRequest,
                                                                @PathVariable UUID restaurantId,
                                                                Principal user) {
        return new ResponseEntity<>(opinionService.addRestaurantOpinion(opinionRequest,restaurantId, user.getName()), HttpStatus.OK);
    }
}
