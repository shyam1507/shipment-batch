package com.spg.batch.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.spg.batch.model.Shipment;

@Mapper
public interface ShipmentMapper {
    void insertShipment(Shipment shipment);
    boolean existsByTrackingNumber(String trackingNumber);
}
