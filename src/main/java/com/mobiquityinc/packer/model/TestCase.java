package com.mobiquityinc.packer.model;

import java.math.BigDecimal;
import java.util.List;

public class TestCase {
	
	private BigDecimal packageWeight;
	private List<Thing> things;
	
	public TestCase(BigDecimal packageWeight, List<Thing> things) {
		this.packageWeight = packageWeight;
		this.things = things;
	}

	public BigDecimal getPackageWeight() {
		return packageWeight;
	}

	public void setPackageWeight(BigDecimal packageWeight) {
		this.packageWeight = packageWeight;
	}

	public List<Thing> getThings() {
		return things;
	}

	public void setThings(List<Thing> things) {
		this.things = things;
	}

	@Override
	public String toString() {
		return "TestCase [packageWeight=" + packageWeight + ", things=" + things + "]";
	}
	
}
