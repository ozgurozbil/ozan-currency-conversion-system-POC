package com.ozan.currency.conversion.system.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversionHistoryDTO extends ConversionDTO {

	private static final long serialVersionUID = 8928141871448302593L;

	@Schema(description = "Source currency code")
	@JsonProperty("sourceCurrency")
	private String sourceCurrency;

	@Schema(description = "Amount of source currency converted")
	@JsonProperty("sourceAmount")
	private BigDecimal sourceAmount;

	@Schema(description = "Target currency code")
	@JsonProperty("targetCurrency")
	private String targetCurrency;

	@Schema(description = "Date & time conversion done")
	@JsonProperty("transactionTime")
	private String transactionTime;
}