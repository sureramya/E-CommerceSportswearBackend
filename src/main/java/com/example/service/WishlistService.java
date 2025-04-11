package com.example.service;

import com.example.entity.Product;
import com.example.entity.WishlistItem;
import com.example.repository.ProductRepository;
import com.example.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    @Autowired private WishlistRepository wishlistRepo;
    @Autowired private ProductRepository productRepo;

    public WishlistItem addToWishlist(String username, Long productId) {
        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) return null;

        WishlistItem item = new WishlistItem();
        item.setUsername(username);
        item.setProduct(product);
        return wishlistRepo.save(item);
    }

    public List<WishlistItem> getWishlist(String username) {
        return wishlistRepo.findByUsername(username);
    }

    public void removeFromWishlist(String username, Long productId) {
        wishlistRepo.deleteByUsernameAndProductId(username, productId);
    }
}
