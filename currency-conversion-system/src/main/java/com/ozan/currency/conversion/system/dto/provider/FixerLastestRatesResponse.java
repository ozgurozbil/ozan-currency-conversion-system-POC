package com.ozan.currency.conversion.system.dto.provider;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FixerLastestRatesResponse extends FixerResponse {
	
	private static final long serialVersionUID = -9217965111239638246L;

	@JsonProperty("timestamp")
	private long timestamp;
	
	@JsonProperty("base")
	private String base;
	
	@JsonProperty("date")
	private String date;
	
	@JsonProperty("rates")
	private Map<String, BigDecimal> rates;
}