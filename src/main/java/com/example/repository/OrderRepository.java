package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUsername(String username);
    List<Order> findAllByUsernameOrderByOrderDateDesc(String username);
    Optional<Order> findTopByUsernameOrderByOrderDateDesc(String username);


    
}

