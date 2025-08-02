// DistanceCalculationService.java
package com.spg.batch.service;

import com.spg.batch.mapper.ShipmentDistanceMapper;
import com.spg.batch.model.Shipment;
import com.spg.batch.model.ShipmentDistance;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DistanceCalculationService {

	private final ShipmentDistanceMapper mapper;

	public DistanceCalculationService(ShipmentDistanceMapper mapper) {
		this.mapper = mapper;
	}

	public void processBatchDistance(List<Shipment> shipments) {
		for (Shipment shipment : shipments) {
			if (shipment.getId() == null) {
				System.err.println("Skipping shipment without ID: " + shipment);
				continue;
			}
			ShipmentDistance distance = new ShipmentDistance();
			distance.setShipmentId(shipment.getId());
			distance.setOrderNumber(shipment.getOrderId());
			distance.setDistanceKm(mockDistance(shipment.getAddress()));
			distance.setCalculatedAt(LocalDateTime.now());
			distance.setCustomerName(shipment.getCustomerName());
			mapper.insertDistance(distance);
		}
	}

	private double mockDistance(String destinationAddress) {
		return Math.round((Math.random() * 800 + 50) * 100.0) / 100.0; // Random km between 50–850
	}
}