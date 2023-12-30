package com.ozan.currency.conversion.system.dto.provider;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FixerResponse implements Serializable {

	private static final long serialVersionUID = 5927532109037943746L;

	@JsonProperty("success")
	private boolean success;
	
	@JsonProperty("error")
	private FixerResponseError error;
}