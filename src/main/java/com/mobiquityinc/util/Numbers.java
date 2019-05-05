package com.mobiquityinc.util;

import java.math.BigDecimal;

public class Numbers {

	private static final Integer MAX_DIGITS = 2;
	
	public static Integer moveFloatingPoint(BigDecimal v) {
		return v.movePointRight(MAX_DIGITS).intValue();
	}
}
