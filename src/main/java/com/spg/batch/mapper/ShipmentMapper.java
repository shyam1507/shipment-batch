package com.spg.batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.spg.batch.model.Shipment;

@Mapper
public interface ShipmentMapper {
    void insertShipment(Shipment shipment);
    boolean existsByTrackingNumber(String trackingNumber);
    List<Shipment> findByBatchNumber(String batchNumber);
}
