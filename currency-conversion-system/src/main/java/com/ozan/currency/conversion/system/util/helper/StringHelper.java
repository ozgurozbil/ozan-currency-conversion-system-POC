package com.ozan.currency.conversion.system.util.helper;

import io.micrometer.common.util.StringUtils;

public class StringHelper {

	public static boolean isNotBlank(String value) {
		return StringUtils.isNotBlank(value);
	}
}