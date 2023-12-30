package com.ozan.currency.conversion.system.dto.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseError implements Serializable {

	private static final long serialVersionUID = -2175523515492434267L;

	@Schema(description = "Unique code of error", example = "ERR001")
	@JsonProperty("errorCode")
	private String errorCode;

	@Schema(description = "Description of error")
	@JsonProperty("errorMessage")
	private String errorMessage;
}