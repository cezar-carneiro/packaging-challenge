package com.mobiquityinc.test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.mobiquityinc.packer.model.TestCase;
import com.mobiquityinc.packer.model.Thing;

public class Instances {

	public static Thing thing(Integer index) {
		return thing(index, null, null);
	}
	
	public static Thing thing(Integer index, String weight, String value) {
		return new Thing(index, weight == null ? null : new BigDecimal(weight), value == null ? null : new BigDecimal(value));
	}
	
	public static TestCase testCase(String capacity, List<Thing> things) {
		return new TestCase( new BigDecimal(capacity), things);
	}
	
	public static List<Thing> listOfThings(Thing... t) {
		return Arrays.asList(t);
	}

}
