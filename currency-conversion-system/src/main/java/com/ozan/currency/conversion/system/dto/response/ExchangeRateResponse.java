package com.ozan.currency.conversion.system.dto.response;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ozan.currency.conversion.system.dto.ExchangeRateDTO;
import com.ozan.currency.conversion.system.util.helper.DateHelper;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateResponse extends Response {

	private static final long serialVersionUID = 3049056556250973728L;
	
	@Schema(description = "Result set given after a successful request")
	@JsonProperty("result")
	private ExchangeRateDTO result;

	@Override
	public ExchangeRateResponse ok() {

		ExchangeRateResponse response = new ExchangeRateResponse();
		response.setStatus(HttpStatus.OK.value());
		response.setTime(DateHelper.getNowAsStr());
		return response;
	}

	@Override
	public ExchangeRateResponse notOk(Integer status) {

		ExchangeRateResponse response = new ExchangeRateResponse();
		response.setStatus(status);
		response.setTime(DateHelper.getNowAsStr());
		return response;
	}

	public ExchangeRateResponse setResult(BigDecimal exchangeRate) {
    	this.result = new ExchangeRateDTO(exchangeRate);
    	return this;
    }
}