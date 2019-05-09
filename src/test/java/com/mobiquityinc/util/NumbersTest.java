package com.mobiquityinc.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class NumbersTest {
	
	public static Stream<Arguments> dataSet() {
		return Stream.of(
				Arguments.of(0, new BigDecimal("0")),
				Arguments.of(0, new BigDecimal("00")),
				Arguments.of(0, new BigDecimal("0.0")),
				Arguments.of(0, new BigDecimal("0.00")),
				Arguments.of(1, new BigDecimal("0.01")),
				Arguments.of(10, new BigDecimal("0.1")),
				Arguments.of(100, new BigDecimal("1")),
				Arguments.of(100, new BigDecimal("1.00")),
				Arguments.of(110, new BigDecimal("1.1")),
				Arguments.of(101, new BigDecimal("1.01")),
				Arguments.of(1000, new BigDecimal("10")),
				Arguments.of(10000, new BigDecimal("100")));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldValidateNullValue() {
		Numbers.moveFloatingPoint(null);
	}
	
	@ParameterizedTest
	@MethodSource("dataSet")
    public void shouldMoveFloatingPointByTwoDecimalPlaces(int expected, BigDecimal param) {
        assertEquals("Should move floating point by two decimal places correctly", expected, Numbers.moveFloatingPoint(param).intValue());
    }
}
