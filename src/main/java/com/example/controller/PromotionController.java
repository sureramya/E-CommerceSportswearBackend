package com.example.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.Promotion;
import com.example.repository.PromotionRepository;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    @Autowired
    private PromotionRepository promotionRepository;

    @GetMapping("/active")
    public List<Promotion> getActivePromotions() {
        LocalDate today = LocalDate.now();
        return promotionRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
   
    public Promotion addPromotion(@RequestBody Promotion promotion) {
        return promotionRepository.save(promotion);
    }
   @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
   
    public void deletePromotion(@PathVariable Long id) {
        promotionRepository.deleteById(id);
    }
   @GetMapping("/validate")
   public ResponseEntity<Promotion> validateCoupon(@RequestParam String code) {
       LocalDate today = LocalDate.now();
       return promotionRepository.findByCode(code)
           .filter(p -> !p.getStartDate().isAfter(today) && !p.getEndDate().isBefore(today)) // check date validity
           .map(ResponseEntity::ok)
           .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
   }
   @GetMapping("/available")
   public List<Promotion> getAvailablePromotions() {
       LocalDate today = LocalDate.now();
       return promotionRepository.findByEndDateAfter(today);
   }


}
