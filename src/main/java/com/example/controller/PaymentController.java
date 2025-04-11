package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.dto.PaymentRequest;
import com.example.entity.Order;
import com.example.repository.OrderRepository;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	@Autowired
	private OrderRepository orderRepository;

	@PostMapping("/payPal")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> processPayPalPayment(@RequestBody PaymentRequest request) {
	    System.out.println("Received PayPal Order ID: " + request.getPaypalOrderId());

	    Order order = orderRepository.findById(request.getOrderId())
	        .orElseThrow(() -> new RuntimeException("Order not found"));
	    

	    Double amount = order.getFinalPrice() != null ? order.getFinalPrice() : order.getTotalAmount(); // ✅ safe fallback

	    System.out.println("Payment amount (from DB): " + amount);

	    if (amount > 0) {
	        // Optionally: order.setStatus("Paid"); orderRepository.save(order);
	        return ResponseEntity.ok("Payment Successful for amount: " + amount);
	    }

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Failed: Invalid amount");
	}


}
