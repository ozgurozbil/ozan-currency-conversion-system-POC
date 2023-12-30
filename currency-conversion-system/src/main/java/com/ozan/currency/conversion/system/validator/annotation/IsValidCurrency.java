package com.ozan.currency.conversion.system.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ozan.currency.conversion.system.validator.CurrencyValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyValidator.class)
@Documented
public @interface IsValidCurrency {

	String message() default "{illegal.argument.default}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
