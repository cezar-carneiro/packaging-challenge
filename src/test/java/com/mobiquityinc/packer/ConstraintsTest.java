package com.mobiquityinc.packer;

import java.math.BigDecimal;

import org.junit.Test;

import com.mobiquityinc.exception.APIException;

public class ConstraintsTest {
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldValidateNullValue() {
		Constraints.check(0, null, "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldValidateNullValue4() {
		Constraints.check(new BigDecimal("0"), null, "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldValidateNullValue2() {
		Constraints.check(((Integer)null), 0, "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldValidateNullValue3() {
		Constraints.check(((BigDecimal)null), 0, "");
	}
	
    @Test(expected = APIException.class)
    public void shouldThrowExceptionForIntegerGreaterThanMax() {
        Constraints.check(2, 1, "");
    }
    
    @Test(expected = Test.None.class)
    public void shouldNotThrowExceptionForIntegerLessThanMax() {
    	Constraints.check(1, 2, "");
    }
    
    @Test(expected = Test.None.class)
    public void shouldNotThrowExceptionForIntegerEqualToMax() {
    	Constraints.check(1, 1, "");
    }

    @Test(expected = APIException.class)
    public void shouldThrowExceptionForBigDecimalGreaterThanMax() {
    	Constraints.check(new BigDecimal("2"), 1, "");
    }
    
    @Test(expected = Test.None.class)
    public void shouldNotThrowExceptionForBigDecimalLessThanMax() {
    	Constraints.check(new BigDecimal("1"), 2, "");
    }
    
    @Test(expected = Test.None.class)
    public void shouldNotThrowExceptionForBigDecimalEqualToMax() {
    	Constraints.check(new BigDecimal("1"), 1, "");
    }
    
}
