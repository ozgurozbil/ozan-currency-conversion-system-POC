package com.ozan.currency.conversion.system.exception;

import org.springframework.http.HttpStatus;

import com.ozan.currency.conversion.system.util.type.ErrorType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConversionProviderException extends RuntimeException {

	private static final long serialVersionUID = -5209700447850727124L;
	
	private ErrorType errorType;
    private HttpStatus httpStatus = HttpStatus.BAD_GATEWAY;
    private String providerErrorMessage = "";
    private int tryCount = 1;

    public ConversionProviderException(ErrorType errorType, int tryCount){

        this.errorType = errorType;
        this.tryCount = tryCount;
    }
    
    public ConversionProviderException(ErrorType errorType, String providerErrorMessage, int tryCount){

        this.errorType = errorType;
        this.providerErrorMessage = providerErrorMessage;
        this.tryCount = tryCount;
    }
}