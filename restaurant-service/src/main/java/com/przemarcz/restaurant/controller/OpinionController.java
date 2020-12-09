package com.przemarcz.restaurant.controller;

import com.przemarcz.restaurant.dto.OpinionDto;
import com.przemarcz.restaurant.service.OpinionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class OpinionController {

    private final OpinionService opinionService;

    @GetMapping("/{restaurantId}/opinions")
    public ResponseEntity<Page<OpinionDto>> getAllRestaurantOpinions(@PathVariable UUID restaurantId, Pageable pageable) {
        return new ResponseEntity<>(opinionService.getAllRestaurantOpinions(restaurantId, pageable), HttpStatus.OK);
    }

    @PostMapping("/{restaurantId}/opinions")
    public ResponseEntity<OpinionDto> addRestaurantOpinion(@RequestBody OpinionDto opinionDto,
                                                           @PathVariable UUID restaurantId, Principal user) {
        return new ResponseEntity<>(opinionService.addRestaurantOpinion(opinionDto,restaurantId, user.getName()), HttpStatus.OK);
    }
}
