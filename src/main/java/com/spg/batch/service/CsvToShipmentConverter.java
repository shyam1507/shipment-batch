package com.spg.batch.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.spg.batch.model.Shipment;

@Component
public class CsvToShipmentConverter {

    public List<Shipment> convert(Message<?> message) throws Exception {
        List<Shipment> shipments = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(message.getPayload().toString()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                Shipment shipment = new Shipment();
                shipment.setOrderId(parts[0]);
                shipment.setCustomerName(parts[1]);
                shipment.setAddress(parts[2]);
                shipment.setStatus(parts[3]);

                // ✅ Generate tracking number
                shipment.setTrackingNumber(generateTrackingNumber());

                // ✅ Generate business key (UUID-based or timestamp-based)
                shipment.setKey(generateUniqueKey());

                shipments.add(shipment);
            }
        }
        reader.close();
        return shipments;
    }

    private String generateTrackingNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "TRK" + datePart + new Random().nextInt(999);
    }

    private String generateUniqueKey() {
        return UUID.randomUUID().toString(); // or use a custom key format
    }
}
