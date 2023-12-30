package com.ozan.currency.conversion.system.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ozan.currency.conversion.system.dto.ConversionHistoryDTO;
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
public class ConversionHistoryResponse extends Response {

	private static final long serialVersionUID = 1252916989576615666L;
	
	@Schema(description = "Result set given after a successful request")
	@JsonProperty("result")
	private List<ConversionHistoryDTO> result;

	@Override
	public ConversionHistoryResponse ok() {

		ConversionHistoryResponse response = new ConversionHistoryResponse();
		response.setStatus(HttpStatus.OK.value());
		response.setTime(DateHelper.getNowAsStr());
		return response;
	}

	@Override
	public ConversionHistoryResponse notOk(Integer status) {

		ConversionHistoryResponse response = new ConversionHistoryResponse();
		response.setStatus(status);
		response.setTime(DateHelper.getNowAsStr());
		return response;
	}
}