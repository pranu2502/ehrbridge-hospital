package com.ehrbridge.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ehrbridge.hospital.config.RSAHelperConfig;

@SpringBootApplication
public class HospitalApplication {

	public static void main(String[] args) {
		RSAHelperConfig.generateAndSetKeys();
		SpringApplication.run(HospitalApplication.class, args);
	}

}
