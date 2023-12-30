package com.ozan.currency.conversion.system.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateDTO implements Serializable {

	private static final long serialVersionUID = 5367286354096293797L;
	
	@Schema(description = "Current exchange rate between provided currencies")
	@JsonProperty("exchangeRate")
	private BigDecimal exchangeRate;
}