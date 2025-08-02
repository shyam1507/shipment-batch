package com.spg.batch.mapper;

import com.spg.batch.model.ShipmentDistance;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShipmentDistanceMapper {
	void insertDistance(ShipmentDistance distance);
}