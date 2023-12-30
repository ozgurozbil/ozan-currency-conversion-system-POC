package com.ozan.currency.conversion.system.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.ozan.currency.conversion.system.exception.ConversionIllegalArgumentException;
import com.ozan.currency.conversion.system.util.type.ErrorType;

public class DateValidatorTest {

	private DateValidator validator = new DateValidator();

	@Test
	public void isValid_shouldReturnTrue_WhenInputCorrect() {

		assertThat(validator.isValid(TRANSACTION_DATE, null)).isTrue();
	}

	@Test
	public void isValid_shouldReturnTrue_WhenInputIsNull() {

		assertThat(validator.isValid(TRANSACTION_DATE_NULL, null)).isTrue();
	}

	@Test
	public void isValid_shouldReturnTrue_WhenInputIsEmpty() {

		assertThat(validator.isValid(TRANSACTION_DATE_EMPTY, null)).isTrue();
	}

	@Test
	public void isValid_shouldThrowConversionIllegalArgumentException_WhenInputIsNotTenCharacters() {

		ConversionIllegalArgumentException exception = Assertions.assertThrows(ConversionIllegalArgumentException.class,
				() -> {

					validator.isValid(TRANSACTION_DATE_NOT_10_CHARACTERS, null);
				});

		assertThat(exception.getErrorType()).isEqualTo(ErrorType.ILLEGAL_ARGUMENT);
		assertThat(exception.getMessageSuffix()).isEqualTo(MESSAGE_SUFFIX);
	}

	@Test
	public void isValid_shouldThrowConversionIllegalArgumentException_WhenInputIsNotInDateFormat() {

		ConversionIllegalArgumentException exception = Assertions.assertThrows(ConversionIllegalArgumentException.class,
				() -> {

					validator.isValid(TRANSACTION_DATE_NOT_IN_DATE_FORMAT, null);
				});

		assertThat(exception.getErrorType()).isEqualTo(ErrorType.ILLEGAL_ARGUMENT);
		assertThat(exception.getMessageSuffix()).isEqualTo(MESSAGE_SUFFIX);
	}

	private static final String TRANSACTION_DATE = "2023-12-28";
	private static final String TRANSACTION_DATE_NULL = null;
	private static final String TRANSACTION_DATE_EMPTY = "";
	private static final String TRANSACTION_DATE_NOT_10_CHARACTERS = "2023-12-1";
	private static final String TRANSACTION_DATE_NOT_IN_DATE_FORMAT = "2023-12-32";
	private static final String MESSAGE_SUFFIX = "transactionDate";
}
