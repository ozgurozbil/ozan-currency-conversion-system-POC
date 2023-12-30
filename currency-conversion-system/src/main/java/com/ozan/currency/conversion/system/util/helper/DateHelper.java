package com.ozan.currency.conversion.system.util.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateHelper {

	private static final String PRETTY_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static String getNowAsStr() {

		return localDateTimeToFormattedStr(getNowAsDateTime());
	}

	public static String localDateTimeToFormattedStr(LocalDateTime dateTime) {

		return Objects.isNull(dateTime) ? null : dateTime.format(DateTimeFormatter.ofPattern(PRETTY_DATE_TIME_PATTERN));
	}

	public static LocalDateTime getNowAsDateTime() {
		return LocalDateTime.now();
	}
	
	public static LocalDate getNowAsDate() {
		return LocalDate.now();
	}
	
	public static LocalDate stringToLocalDate(String date) {
		return LocalDate.parse(date);
	}
}