package com.ozan.currency.conversion.system.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversionDTO implements Serializable {

	private static final long serialVersionUID = 1586612367291468501L;

	@Schema(description = "Conversion transaction unique identifier")
	@JsonProperty("transactionId")
    private Long transactionId;

	@Schema(description = "Equivalent amount in target currency")
	@JsonProperty("targetAmount")
    private BigDecimal targetAmount;
}