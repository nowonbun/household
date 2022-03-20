package com.nowonbun.household;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "com.nowonbun.household.*")
public class HouseholdApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseholdApplication.class, args);
	}

}
