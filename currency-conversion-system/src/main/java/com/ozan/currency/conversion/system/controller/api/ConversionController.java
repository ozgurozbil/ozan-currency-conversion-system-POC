package com.ozan.currency.conversion.system.controller.api;

import java.math.BigDecimal;

import org.springframework.validation.annotation.Validated;

import com.ozan.currency.conversion.system.dto.response.ConversionHistoryResponse;
import com.ozan.currency.conversion.system.dto.response.ConvertCurrencyResponse;
import com.ozan.currency.conversion.system.dto.response.Response;
import com.ozan.currency.conversion.system.validator.annotation.IsValidCurrency;
import com.ozan.currency.conversion.system.validator.annotation.IsValidDate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Validated
@Tag(name = "Conversion API")
public interface ConversionController {

	@Operation(summary = "Web service for retrieving equivalent amount of target currency", description = "Service calculates equivalent amount of target currency with given source currency and amount. Also saves conversion action to DB.")
	@ApiResponses({ @ApiResponse(responseCode = "200", content = {
			@Content(schema = @Schema(implementation = ConvertCurrencyResponse.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", content = {
					@Content(schema = @Schema(implementation = Response.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "502", content = {
					@Content(schema = @Schema(implementation = Response.class), mediaType = "application/json") }) })
	public ConvertCurrencyResponse convertCurrency(
			@Parameter(description = "Source amount of currency to be converted") @Digits(integer = 9, fraction = 2, message = "{illegal.argument.amount.format}") @DecimalMin(value = "0.01", message = "{illegal.argument.amount.min}") BigDecimal amount,
			@Parameter(description = "Source currency code", example = "TRY") @IsValidCurrency String sourceCurrency,
			@Parameter(description = "Target currency code", example = "TRY") @IsValidCurrency String targetCurrency);

	@Operation(summary = "Web service for listing conversion history records", description = "Service returns exact conversion record when transaction id provided. Otherwise all conversion records of given transaction date will be returned with pagination.")
	@ApiResponses({ @ApiResponse(responseCode = "200", content = {
			@Content(schema = @Schema(implementation = ConversionHistoryResponse.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", content = {
					@Content(schema = @Schema(implementation = Response.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", content = {
					@Content(schema = @Schema(implementation = Response.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "502", content = {
					@Content(schema = @Schema(implementation = Response.class), mediaType = "application/json") }) })
	public ConversionHistoryResponse getHistory(
			@Parameter(description = "Unique identifier of conversion", example = "123") @Min(value = 1, message = "{illegal.argument.gte.1}") Long transactionId,
			@Parameter(description = "Date of conversions(YYYY-MM-DD)", example = "2023-12-28") @IsValidDate String transactionDate,
			@Parameter(description = "Page number for paginator") @Min(value = 0, message = "{illegal.argument.gte.0}") @Max(value = 100000, message = "{illegal.argument.lte.100000}") Integer pageNumber,
			@Parameter(description = "Page size for paginator") @Min(value = 1, message = "{illegal.argument.gte.1}") @Max(value = 100, message = "{illegal.argument.lte.100}") Integer pageSize);
}