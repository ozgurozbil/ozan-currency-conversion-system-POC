package com.ozan.currency.conversion.system.dto.response;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ozan.currency.conversion.system.dto.ConversionDTO;
import com.ozan.currency.conversion.system.util.helper.DateHelper;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConvertCurrencyResponse extends Response {

	private static final long serialVersionUID = 8218488902927193665L;
	
	@Schema(description = "Result set given after a successful request")
	@JsonProperty("result")
	private ConversionDTO result;

	@Override
	public ConvertCurrencyResponse ok() {

		ConvertCurrencyResponse response = new ConvertCurrencyResponse();
		response.setStatus(HttpStatus.OK.value());
		response.setTime(DateHelper.getNowAsStr());
		return response;
	}

	@Override
	public ConvertCurrencyResponse notOk(Integer status) {

		ConvertCurrencyResponse response = new ConvertCurrencyResponse();
		response.setStatus(status);
		response.setTime(DateHelper.getNowAsStr());
		return response;
	}
}