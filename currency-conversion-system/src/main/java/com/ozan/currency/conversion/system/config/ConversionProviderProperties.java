package com.ozan.currency.conversion.system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "conversion.provider.fixer")
public class ConversionProviderProperties {

	private String baseUrl;
	private String accessKey;
	private String lastestRates;
	
	public String getLastestRatesEndpoint() {
		return baseUrl + lastestRates + "?access_key=" + accessKey;
	}
}