package com.ozan.currency.conversion.system.util.helper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DateHelperTest {

	@Test
	public void getNowAsStr_shouldReturnStringNotBlank() {

		assertThat(DateHelper.getNowAsStr()).isNotBlank();
	}

	@Test
	public void getNowAsDateTime_shouldReturnLocalDateTimeNonNull() {

		assertThat(DateHelper.getNowAsDateTime()).isNotNull();
	}

	@Test
	public void getNowAsDate_shouldReturnLocalDateNonNull() {

		assertThat(DateHelper.getNowAsDate()).isNotNull();
	}

	@Test
	public void stringToLocalDate_shouldReturnLocalDateTimeNonNull() {

		LocalDate date = DateHelper.stringToLocalDate(FORMATTED_LOCAL_DATE);

		assertThat(date).isNotNull();
		assertThat(date.getYear()).isEqualTo(YEAR);
		assertThat(date.getMonthValue()).isEqualTo(MONTH);
		assertThat(date.getDayOfMonth()).isEqualTo(DAY);
	}

	@Test
	public void localDateTimeToFormattedStr_shouldReturnStringValue_whenParameterIsFine() {

		assertThat(DateHelper.localDateTimeToFormattedStr(LOCAL_DATE_TIME_20100101))
				.isEqualTo(FORMATTED_DATE_TIME_STING_20100101);
	}

	@Test
	public void localDateTimeToFormattedStr_shouldReturnNull_whenParameterIsNull() {

		assertThat(DateHelper.localDateTimeToFormattedStr(null)).isNull();
	}

	private static final LocalDateTime LOCAL_DATE_TIME_20100101 = LocalDateTime.of(2010, 1, 1, 10, 1, 5);
	private static final String FORMATTED_DATE_TIME_STING_20100101 = "2010-01-01 10:01:05";
	private static final Integer YEAR = 2023;
	private static final Integer MONTH = 12;
	private static final Integer DAY = 29;
	private static final String FORMATTED_LOCAL_DATE = YEAR + "-" + MONTH + "-" + DAY;
}