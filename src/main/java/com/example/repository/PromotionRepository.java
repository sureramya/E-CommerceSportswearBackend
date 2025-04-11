package com.example.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
	List<Promotion> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate start, LocalDate end);
	  Optional<Promotion> findByCode(String code);
	  List<Promotion> findByEndDateAfter(LocalDate date);

}
