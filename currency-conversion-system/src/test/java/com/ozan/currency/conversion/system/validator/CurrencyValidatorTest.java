package com.ozan.currency.conversion.system.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.ozan.currency.conversion.system.exception.ConversionIllegalArgumentException;
import com.ozan.currency.conversion.system.util.type.ErrorType;

public class CurrencyValidatorTest {

	private CurrencyValidator validator = new CurrencyValidator();

	@Test
	public void isValid_shouldReturnTrue_WhenInputIsThreeLetters() {

		assertThat(validator.isValid(CURRENCY_TRY, null)).isTrue();
	}

	@Test
	public void isValid_shouldThrowConversionIllegalArgumentException_WhenInputIsNotThreeCharacters() {

		ConversionIllegalArgumentException exception = Assertions.assertThrows(ConversionIllegalArgumentException.class,
				() -> {

					validator.isValid(CURRENCY_INVALID_TWO_LETTER, null);
				});

		assertThat(exception.getErrorType()).isEqualTo(ErrorType.ILLEGAL_ARGUMENT);
		assertThat(exception.getMessageSuffix()).isEqualTo(MESSAGE_SUFFIX);
	}

	@Test
	public void isValid_shouldThrowConversionIllegalArgumentException_WhenInputContainsNonLetterCharacters() {

		ConversionIllegalArgumentException exception = Assertions.assertThrows(ConversionIllegalArgumentException.class,
				() -> {

					validator.isValid(CURRENCY_INVALID_NUMERIC, null);
				});

		assertThat(exception.getErrorType()).isEqualTo(ErrorType.ILLEGAL_ARGUMENT);
		assertThat(exception.getMessageSuffix()).isEqualTo(MESSAGE_SUFFIX);
	}

	private static final String CURRENCY_TRY = "TRY";
	private static final String CURRENCY_INVALID_TWO_LETTER = "TR";
	private static final String CURRENCY_INVALID_NUMERIC = "T_1";
	private static final String MESSAGE_SUFFIX = "currencyCode";
}
