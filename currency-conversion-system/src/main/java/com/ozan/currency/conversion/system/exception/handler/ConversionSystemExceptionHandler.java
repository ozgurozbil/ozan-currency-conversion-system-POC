package com.ozan.currency.conversion.system.exception.handler;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ozan.currency.conversion.system.config.MessagePropertiesConfig;
import com.ozan.currency.conversion.system.dto.response.Response;
import com.ozan.currency.conversion.system.exception.ConversionIllegalArgumentException;
import com.ozan.currency.conversion.system.exception.ConversionNotFoundException;
import com.ozan.currency.conversion.system.exception.ConversionProviderException;
import com.ozan.currency.conversion.system.exception.CurrencyNotFoundException;
import com.ozan.currency.conversion.system.util.type.ErrorType;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@AllArgsConstructor
public class ConversionSystemExceptionHandler extends ResponseEntityExceptionHandler {

	private final MessagePropertiesConfig propertiesConfig;

	@ExceptionHandler(ConversionNotFoundException.class)
	public final ResponseEntity handleNotFoundException(ConversionNotFoundException e) {

		Response response = new Response().notOk(e.getHttpStatus().value());
		if (Objects.nonNull(e.getTransactionId())) {

			log.error("Transaction not found with given transaction id: " + e.getTransactionId());

			response.addErrorMsg(e.getErrorType().getCode(), formatErrorMessage(
					e.getErrorType().getDescription().concat(".transactionid"), e.getTransactionId().toString()));
		} else {

			log.error("Transaction not found with given transaction date: " + e.getTransactionDate() + ", page number: "
					+ e.getPageNumber() + ", page size: " + e.getPageSize());

			response.addErrorMsg(e.getErrorType().getCode(),
					formatErrorMessage(e.getErrorType().getDescription().concat(".transactiondate"),
							e.getTransactionDate(), e.getPageNumber(), e.getPageSize()));
		}

		return new ResponseEntity(response, e.getHttpStatus());
	}

	@ExceptionHandler(ConversionProviderException.class)
	public final ResponseEntity handleConversionProviderException(ConversionProviderException e) {

		log.error("Error occured while calling provider endpoint. " + e.getProviderErrorMessage() + " tryCount: "
				+ e.getTryCount(), e);

		Response response = new Response().notOk(e.getHttpStatus().value());
		response.addErrorMsg(e.getErrorType().getCode(), formatErrorMessage(e.getErrorType().getDescription()));
		return new ResponseEntity(response, e.getHttpStatus());
	}

	@ExceptionHandler(CurrencyNotFoundException.class)
	public final ResponseEntity handleCurrencyNotFoundException(CurrencyNotFoundException e) {

		log.error("No exchange rate can be found with currency: " + e.getCurrency());

		Response response = new Response().notOk(e.getHttpStatus().value());
		response.addErrorMsg(e.getErrorType().getCode(),
				formatErrorMessage(e.getErrorType().getDescription(), e.getCurrency()));
		return new ResponseEntity(response, e.getHttpStatus());
	}

	@ExceptionHandler(ConversionIllegalArgumentException.class)
	public final ResponseEntity handleConversionIllegalArgumentException(ConversionIllegalArgumentException e) {

		Response response = new Response().notOk(e.getHttpStatus().value());
		response.addErrorMsg(e.getErrorType().getCode(),
				formatErrorMessage(e.getErrorType().getDescription() + e.getMessageSuffix()));
		return new ResponseEntity(response, e.getHttpStatus());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {

		Response response = new Response().notOk(HttpStatus.BAD_REQUEST.value());
		response.addErrorMsg(ErrorType.ILLEGAL_ARGUMENT.getCode(), e.getMessage());
		return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
	}

	private String formatErrorMessage(String messageTemplate, String... args) {

		Optional<String> templateContent = Optional.ofNullable(propertiesConfig.getConfigValue(messageTemplate));

		return templateContent.isPresent() ? MessageFormat.format(templateContent.get(), args)
				: "Generic error occured. Please check given values and try again later.";
	}
}