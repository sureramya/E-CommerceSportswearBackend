package com.example.repository;



import com.example.entity.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	   List<Product> findByCategory(String category);
	    List<Product> findByBrand(String brand);
	    List<Product> findByNameContainingIgnoreCase(String name);
	    List<Product> findByRatingGreaterThanEqual(double rating);
	    List<Product> findByTag(String tag);
	    List<Product> findByStockGreaterThan(int stock);


	    				

}
