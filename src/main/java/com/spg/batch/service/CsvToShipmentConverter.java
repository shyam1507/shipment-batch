package com.spg.batch.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.spg.batch.model.Shipment;

@Component
public class CsvToShipmentConverter {

    // You can also persist this in DB if the app restarts
    private static final Set<String> processedFiles = ConcurrentHashMap.newKeySet();

    public List<Shipment> convert(Message<?> message) throws Exception {
        String filePath = message.getPayload().toString();
        String fileHash = generateFileHash(filePath);

        // Skip if file already processed
        if (!processedFiles.add(fileHash)) {
            System.out.println("Skipping already processed file: " + filePath);
            return Collections.emptyList();
        }

        List<Shipment> shipments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    Shipment shipment = new Shipment();
                    shipment.setOrderId(parts[0]);
                    shipment.setCustomerName(parts[1]);
                    shipment.setAddress(parts[2]);
                    shipment.setStatus(parts[3]);

                    shipment.setTrackingNumber(generateTrackingNumber());
                    shipment.setKey(generateShipmentKey());

                    shipments.add(shipment);
                }
            }
        }

        return shipments;
    }

    private String generateFileHash(String filePath) throws Exception {
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(fileBytes);
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    private String generateTrackingNumber() {
        String uuidPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "TRK" + System.currentTimeMillis() + "-" + uuidPart;
    }

    private String generateShipmentKey() {
        return "SHIP-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}