package com.example.dto;

import lombok.Data;

@Data
	public class CartRequest {
	    private String username;
	    private Long productId;
	    private int quantity;
	}


