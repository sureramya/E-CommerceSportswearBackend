package com.example.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.CartRequest;
import com.example.entity.CartItem;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UserRepository userRepo;

    public CartItem addToCart(String username, Long productId, int quantity) {
        User user = userRepo.findByUsername(username);
        Product product = productRepo.findById(productId).orElse(null);
        if (user == null || product == null) return null;

        CartItem item = new CartItem(null, user, product, quantity);
        return cartRepo.save(item);
    }

    public List<CartItem> getCartItems(String username) {
        User user = userRepo.findByUsername(username);
        return cartRepo.findByUser(user);
    }

    public void updateCartItemQuantity(String username, Long productId, int quantity) {
        User user = userRepo.findByUsername(username);
        Product product = productRepo.findById(productId).orElse(null);

        if (user != null && product != null) {
            CartItem item = cartRepo.findByUserAndProduct(user, product);
            if (item != null) {
                item.setQuantity(quantity);
                cartRepo.save(item);
            }
        }
    }

    
    public int countItems(String username) {
        User user = userRepo.findByUsername(username);
        return cartRepo.countByUser(user);
    }
    public void removeItem(String username, Long productId) {
        User user = userRepo.findByUsername(username);
        Product product = productRepo.findById(productId).orElse(null);
        if (user != null && product != null) {
            cartRepo.deleteByUserAndProduct(user, product);
        }
    }

    public void clearCart(String username) {
        User user = userRepo.findByUsername(username);
        if (user != null) {
            List<CartItem> items = cartRepo.findByUser(user);
            cartRepo.deleteAll(items);
        }
    }
}
