package com.spg.batch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:integration/integration-config.xml")
@MapperScan("com.spg.batch.mapper")
public class ShipmentBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShipmentBatchApplication.class, args);
	}

}
