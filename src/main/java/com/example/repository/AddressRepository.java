package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserUsername(String username);
}
