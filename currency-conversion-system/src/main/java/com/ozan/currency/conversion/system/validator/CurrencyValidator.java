package com.ozan.currency.conversion.system.validator;

import com.ozan.currency.conversion.system.exception.ConversionIllegalArgumentException;
import com.ozan.currency.conversion.system.util.type.ErrorType;
import com.ozan.currency.conversion.system.validator.annotation.IsValidCurrency;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CurrencyValidator implements ConstraintValidator<IsValidCurrency, String> {

	@Override
	public void initialize(IsValidCurrency isValidCurrency) {

		isValidCurrency.message();
	}

	@Override
	public boolean isValid(String currencyCode, ConstraintValidatorContext context) {

		if (currencyCode.length() != 3 || !currencyCode.chars().allMatch(Character::isLetter)) {
			throw new ConversionIllegalArgumentException(ErrorType.ILLEGAL_ARGUMENT, "currencyCode");
		}

		return true;
	}

}
