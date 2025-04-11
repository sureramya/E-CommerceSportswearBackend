package com.example.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private String category;
    private String brand;
    private String size;
    private String color;
    private String imageUrl;
    private int stock;
    private double rating;  // range: 0.0 - 5.0
    private String tag;
    
}
