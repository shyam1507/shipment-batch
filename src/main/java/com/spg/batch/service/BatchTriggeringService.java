// BatchTriggeringService.java
package com.spg.batch.service;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import com.spg.batch.model.Shipment;

import jakarta.annotation.PostConstruct;

@Service
public class BatchTriggeringService {

	@Autowired
	private CsvToShipmentConverter converter;
	@Autowired
	private ShipmentService shipmentService;
	@Autowired
	private DistanceCalculationService distanceCalculationService;

	@PostConstruct
	public void init() {
		new Thread(() -> {
			while (true) {
				try {
					processBatchFolder();
					TimeUnit.MINUTES.sleep(5); // Every 5 minutes
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void processBatchFolder() throws Exception {
		File folder = new File("./src/main/resources/sample_shipments");
		File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));
		if (files == null)
			return;

		for (File file : files) {
			List<Shipment> shipments = converter.convert(new GenericMessage<>(file.getAbsolutePath()));
			if (!shipments.isEmpty()) {
				String batchNumber = shipments.get(0).getBatchNumber();
				shipmentService.saveAll(shipments);
				Thread.sleep(1000);
				List<Shipment> savedShipments = shipmentService.getShipmentsByBatchNumber(batchNumber);
				distanceCalculationService.processBatchDistance(savedShipments);
			}
		}
	}
}