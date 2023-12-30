package com.ozan.currency.conversion.system.util.type;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class ErrorTypeTest {

	@Test
	public void values_shouldReturnSuccess_whenAllElementsHaveCodeValue() {

		Stream.of(ErrorType.values()).forEachOrdered(value -> assertThat(value.getCode()).isNotBlank());
	}

	@Test
	public void values_shouldReturnSuccess_whenAllElementsHaveDescriptionValue() {

		Stream.of(ErrorType.values()).forEachOrdered(value -> assertThat(value.getDescription()).isNotBlank());
	}
}