package com.ozan.currency.conversion.system.controller.api;

import com.ozan.currency.conversion.system.dto.response.ExchangeRateResponse;
import com.ozan.currency.conversion.system.dto.response.Response;
import com.ozan.currency.conversion.system.validator.annotation.IsValidCurrency;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Exchange Rate API")
public interface ExchangeRateController {

	@Operation(summary = "Web service for retrieving exchange rate between given currency codes")
	@ApiResponses({ @ApiResponse(responseCode = "200", content = {
			@Content(schema = @Schema(implementation = ExchangeRateResponse.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", content = {
					@Content(schema = @Schema(implementation = Response.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "502", content = {
					@Content(schema = @Schema(implementation = Response.class), mediaType = "application/json") }) })
	public ExchangeRateResponse getExchangeRate(
			@Parameter(description = "Source currency code", example = "TRY") @IsValidCurrency String sourceCurrency,
			@Parameter(description = "Target currency code", example = "TRY") @IsValidCurrency String targetCurrency);
}