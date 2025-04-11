package com.example.repository;

import com.example.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByUsername(String username);
    boolean existsByUsernameAndProductId(String username, Long productId);
    void deleteByUsernameAndProductId(String username, Long productId);
}
