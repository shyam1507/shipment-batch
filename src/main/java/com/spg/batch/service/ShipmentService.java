package com.spg.batch.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spg.batch.mapper.ShipmentMapper;
import com.spg.batch.model.Shipment;

@Service
public class ShipmentService {

    private final ShipmentMapper mapper;

    public ShipmentService(ShipmentMapper mapper) {
        this.mapper = mapper;
    }

    public void saveAll(List<Shipment> shipments) {
        for (Shipment s : shipments) {
            mapper.insertShipment(s);
        }
    }
}