	package com.example.controller;
	
	import java.util.List;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.ResponseEntity;
	import org.springframework.security.access.prepost.PreAuthorize;
	import org.springframework.transaction.annotation.Transactional;
	import org.springframework.web.bind.annotation.*;
	
	import com.example.dto.CartRequest;
	import com.example.entity.CartItem;
	import com.example.service.CartService;
	
	@RestController
	@RequestMapping("/api/cart")
	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('USER')")
	public class CartController {
	
	    @Autowired
	    private CartService cartService;
	
	    @PostMapping("/add")
	    public CartItem addToCart(@RequestBody CartRequest request) {
	        return cartService.addToCart(request.getUsername(), request.getProductId(), request.getQuantity());
	    }
	
	    @GetMapping("/{username}")
	    public List<CartItem> getCartItems(@PathVariable String username) {
	        return cartService.getCartItems(username);
	    }
	  
	    @DeleteMapping("/remove")
	    @Transactional
	    public ResponseEntity<String> removeFromCart(@RequestParam String username, @RequestParam Long productId) {
	        cartService.removeItem(username, productId);
	        return ResponseEntity.ok("Item deleted successfully");
	    }
	    @PutMapping("/update")
	    @Transactional
	    public ResponseEntity<List<CartItem>> updateCartItem(@RequestBody CartRequest request) {
	        cartService.updateCartItemQuantity(request.getUsername(), request.getProductId(), request.getQuantity());
	        List<CartItem> updatedCartItems = cartService.getCartItems(request.getUsername());
	        return ResponseEntity.ok(updatedCartItems);
	    }
	
	    @DeleteMapping("/clear")
	    @Transactional
	    public ResponseEntity<String> clearCart(@RequestParam String username) {
	        cartService.clearCart(username);
	        return ResponseEntity.ok("Cart cleared successfully");
	    }
	    @GetMapping("/count")
	    public ResponseEntity<Integer> getCartCount(@RequestParam String username) {
	        int count = cartService.countItems(username);
	        return ResponseEntity.ok(count);
	    }
	}
