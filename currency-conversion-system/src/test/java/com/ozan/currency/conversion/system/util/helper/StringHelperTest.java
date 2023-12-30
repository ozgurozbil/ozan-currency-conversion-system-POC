package com.ozan.currency.conversion.system.util.helper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class StringHelperTest {

	@Test
	public void isNotBlank_shouldReturnFalse_whenParameterIsNull() {

		assertThat(StringHelper.isNotBlank(null)).isFalse();
	}

	@Test
	public void isNotBlank_shouldReturnFalse_whenParameterIsEmptyStr() {

		assertThat(StringHelper.isNotBlank(EMPTY_STR)).isFalse();
	}

	@Test
	public void isNotBlank_shouldReturnTrue_whenParameterIsFine() {

		assertThat(StringHelper.isNotBlank(STR_LENGTH_4)).isTrue();
	}

	private static final String STR_LENGTH_4 = "XXXX";
	private static final String EMPTY_STR = "";
}