package com.ozan.currency.conversion.system.exception;

import org.springframework.http.HttpStatus;

import com.ozan.currency.conversion.system.util.type.ErrorType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConversionIllegalArgumentException extends RuntimeException {

	private static final long serialVersionUID = 3588543833011326475L;

	private ErrorType errorType;
	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
	private String messageSuffix;

	public ConversionIllegalArgumentException(ErrorType errorType, String messageSuffix) {

		this.errorType = errorType;
		this.messageSuffix = messageSuffix;
	}
}