package com.spg.batch.model;

import lombok.Data;

@Data
public class Shipment {
	private Long id;
	private String orderId;
	private String customerName;
	private String address;
	private String status;
	private String trackingNumber;  // unique
	private String key;
}