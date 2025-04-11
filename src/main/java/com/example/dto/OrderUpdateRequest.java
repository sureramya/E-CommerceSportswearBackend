package com.example.dto;

public class OrderUpdateRequest {
	 private Long orderId;
	    private String status;
		public Long getOrderId() {
			return orderId;
		}
		public void setOrderId(Long orderId) {
			this.orderId = orderId;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}  
	    
}
