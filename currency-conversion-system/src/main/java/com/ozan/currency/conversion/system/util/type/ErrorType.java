package com.ozan.currency.conversion.system.util.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

	GENERIC_INTERNAL_ERROR("ERR001","generic.error"),
	ILLEGAL_ARGUMENT("ERR002", "illegal.argument."),
	TRANSACTION_NOT_FOUND("ERR003", "transaction.not.found"),
	CONVERSION_PROVIDER_ERROR("ERR004", "conversation.provider.error"),
	CURRENCY_NOT_FOUND_EXCEPTION("ERR005", "currency.not.found");
	
    private String code;
    private String description;
}