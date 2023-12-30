package com.ozan.currency.conversion.system.exception;

import org.springframework.http.HttpStatus;

import com.ozan.currency.conversion.system.util.type.ErrorType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 427549491953752621L;
	
	private ErrorType errorType;
	private HttpStatus httpStatus = HttpStatus.NOT_FOUND;
	private String currency;
	
	public CurrencyNotFoundException(ErrorType errorType, String currency) {

		this.errorType = errorType;
		this.currency = currency;
	}
}