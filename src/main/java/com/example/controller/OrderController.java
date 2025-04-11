package com.example.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.OrderDetailsDto;
import com.example.dto.OrderUpdateRequest;
import com.example.entity.Order;
import com.example.repository.OrderRepository;
import com.example.repository.PromotionRepository;
import com.example.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PromotionRepository promotionRepository;



    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(
            @RequestParam String username,
            @RequestParam Long addressId,
            @RequestParam(required = false) String promotionCode) {
        
        return ResponseEntity.ok(orderService.placeOrder(username, addressId, promotionCode));
    }
    @GetMapping("/latest/{username}")
    public ResponseEntity<Order> getLatestOrder(@PathVariable String username) {
        return orderRepository.findTopByUsernameOrderByOrderDateDesc(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/{username}")
    public ResponseEntity<List<Order>> getOrders(@PathVariable String username) {
        return ResponseEntity.ok(orderService.getUserOrders(username));
    }
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus("CANCELLED");
            orderRepository.save(order);
            return ResponseEntity.ok("Order cancelled successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/place-direct")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> payload, Principal principal) {
        try {
            Long addressId = Long.parseLong(payload.get("addressId").toString());
            String promotionCode = (String) payload.getOrDefault("promotionCode", null);

            Order order = orderService.placeOrder(principal.getName(), addressId, promotionCode);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user")
    public List<Order> getUserOrders(@RequestParam String username) {
        return orderRepository.findAllByUsernameOrderByOrderDateDesc(username);
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/updateStatus")
    public ResponseEntity<String> updateOrderStatus(@RequestBody OrderUpdateRequest request) {
        try {
            orderService.updateOrderStatus(request.getOrderId(), request.getStatus());
            return ResponseEntity.ok("Order status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update order status");
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }



}
