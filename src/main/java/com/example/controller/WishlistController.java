package com.example.controller;

import com.example.entity.WishlistItem;
import com.example.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    // ➕ Add to Wishlist
    @PostMapping("/add")
    public ResponseEntity<WishlistItem> addToWishlist(@RequestParam String username, @RequestParam Long productId) {
        WishlistItem item = wishlistService.addToWishlist(username, productId);
        return ResponseEntity.ok(item);
    }

    // 📄 Get Wishlist for a User
    @GetMapping("/{username}")
    public ResponseEntity<List<WishlistItem>> getWishlist(@PathVariable String username) {
        List<WishlistItem> wishlist = wishlistService.getWishlist(username);
        return ResponseEntity.ok(wishlist);
    }

    // ❌ Remove from Wishlist
    @Transactional
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromWishlist(@RequestParam String username, @RequestParam Long productId) {
        wishlistService.removeFromWishlist(username, productId);
        return ResponseEntity.ok("Removed from wishlist");
    }
}
