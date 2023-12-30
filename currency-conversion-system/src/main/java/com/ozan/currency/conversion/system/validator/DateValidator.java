package com.ozan.currency.conversion.system.validator;

import org.hibernate.internal.util.StringHelper;

import com.ozan.currency.conversion.system.exception.ConversionIllegalArgumentException;
import com.ozan.currency.conversion.system.util.helper.DateHelper;
import com.ozan.currency.conversion.system.util.type.ErrorType;
import com.ozan.currency.conversion.system.validator.annotation.IsValidDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<IsValidDate, String> {

    @Override
    public void initialize(IsValidDate isValidDate) {

    	isValidDate.message();
    }
	
	@Override
	public boolean isValid(String date, ConstraintValidatorContext context) {

		if (StringHelper.isBlank(date)) {
			return true;
		}
		
		if (date.length() != 10) {
			throw new ConversionIllegalArgumentException(ErrorType.ILLEGAL_ARGUMENT, "transactionDate");
		}
		
		try {
			DateHelper.stringToLocalDate(date);
		} catch (Exception e) {
			throw new ConversionIllegalArgumentException(ErrorType.ILLEGAL_ARGUMENT, "transactionDate");
		}
		
		return true;
	}

}
