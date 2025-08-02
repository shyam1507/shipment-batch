package com.spg.batch.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShipmentDistance {
	private Long id; // PK
	private Long shipmentId; // FK from shipment table
	private String orderNumber;
	private Double distanceKm;
	private LocalDateTime calculatedAt;
	private String customerName;
}
