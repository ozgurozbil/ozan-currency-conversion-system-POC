package com.ozan.currency.conversion.system.dto.provider;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FixerResponseError implements Serializable {

	private static final long serialVersionUID = -4939064607110780270L;

	@JsonProperty("code")
	private int code;

	@JsonProperty("info")
	private String info;
}