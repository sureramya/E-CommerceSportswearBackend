package com.example.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.CartItem;
import com.example.entity.Product;
import com.example.entity.User;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    void deleteByUserAndProduct(User user, Product product);
    CartItem findByUserAndProduct(User user, Product product);
    int countByUser(User user);

}
