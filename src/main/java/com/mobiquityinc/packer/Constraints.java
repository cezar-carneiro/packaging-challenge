package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;

/**
 * 
 * @author cezar.carneiro
 *
 */
public class Constraints {
	
	public static final Integer MAX_PACKAGE_WEIGHT = 100;
	public static final Integer MAX_THING_WEIGHT = 100;
	public static final Integer MAX_THING_COST = 100;
	public static final Integer MAX_THINGS = 15;
	
	public static void check(Integer value, Integer max, String error) {
		if (value > max) {
			throw new APIException(error);
		}
	}
	
	public static void check(Double value, Integer max, String error) {
		if (value > max) {
			throw new APIException(error);
		}
	}

}
