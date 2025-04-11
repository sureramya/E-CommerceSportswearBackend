package com.example.service;


import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.entity.CartItem;
import com.example.repository.AddressRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.repository.PromotionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
	@Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

	@Autowired
	private PromotionRepository promotionRepository; 
	 @Autowired
	    private AddressRepository addressRepo;
	 public Order placeOrder(String username, Long addressId, String promotionCode) {
		    List<CartItem> cartItems = cartService.getCartItems(username);

		    Order order = new Order();
		    order.setUsername(username);
		    order.setOrderDate(LocalDateTime.now());

		    addressRepo.findById(addressId).ifPresent(order::setShippingAddress);

		    double total = 0.0;
		    List<OrderItem> orderItems = new ArrayList<>();
		    for (CartItem cartItem : cartItems) {
		        OrderItem item = new OrderItem();
		        item.setOrder(order);
		        item.setProductId(cartItem.getProduct().getId());
		        item.setProductName(cartItem.getProduct().getName());
		        item.setQuantity(cartItem.getQuantity());
		        item.setPrice(cartItem.getProduct().getPrice());

		        orderItems.add(item);
		        total += item.getQuantity() * item.getPrice();
		    }

		    double discountAmount = 0.0;
		    if (promotionCode != null && !promotionCode.isEmpty()) {
		        var optionalPromotion = promotionRepository.findByCode(promotionCode);
		        if (optionalPromotion.isPresent()) {
		            var promotion = optionalPromotion.get();
		            discountAmount = total * promotion.getDiscountPercentage() / 100.0;
		            total -= discountAmount;
		            order.setCouponCode(promotion.getCode()); // Optional
		        }
		    }

		    order.setItems(orderItems);
		    order.setTotalAmount(total + discountAmount); // original total before discount
		    order.setDiscountAmount(discountAmount);      // new field
		    order.setFinalPrice(total);                   // ✅ very important
		    order.setStatus("Placed");

		    cartService.clearCart(username);

		    return orderRepository.save(order);
		}

	 public List<Order> getUserOrders(String username) {
	        return orderRepository.findByUsername(username);
	    }
	  public void updateOrderStatus(Long orderId, String status) {
	        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
	        order.setStatus(status);
	        orderRepository.save(order);
	    }
}
