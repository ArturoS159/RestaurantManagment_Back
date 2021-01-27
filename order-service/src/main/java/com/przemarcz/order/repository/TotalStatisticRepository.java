package com.przemarcz.order.repository;

import com.przemarcz.order.model.TotalStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TotalStatisticRepository extends JpaRepository<TotalStatistic, UUID> {
}
