package com.ozan.currency.conversion.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:provider.properties")
@ConfigurationPropertiesScan
@SpringBootApplication
public class CurrencyConversionSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyConversionSystemApplication.class, args);
	}
}