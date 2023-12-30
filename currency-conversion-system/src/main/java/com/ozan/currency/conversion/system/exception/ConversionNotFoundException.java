package com.ozan.currency.conversion.system.exception;

import org.springframework.http.HttpStatus;

import com.ozan.currency.conversion.system.util.type.ErrorType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConversionNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 2347159597067840720L;
	
	private ErrorType errorType;
	private HttpStatus httpStatus = HttpStatus.NOT_FOUND;
	private Long transactionId;
	private String transactionDate;
	private String pageNumber;
	private String pageSize;

	public ConversionNotFoundException(ErrorType errorType, Long transactionId) {

		this.errorType = errorType;
		this.transactionId = transactionId;
	}

	public ConversionNotFoundException(ErrorType errorType, String transactionDate, String pageNumber, String pageSize) {

		this.errorType = errorType;
		this.transactionDate = transactionDate;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}
}