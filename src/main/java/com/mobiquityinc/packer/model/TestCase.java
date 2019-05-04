package com.mobiquityinc.packer.model;

import java.util.List;

public class TestCase {
	
	private Integer packageWeight;
	private List<Thing> things;
	
	public TestCase(Integer packageWeight, List<Thing> things) {
		this.packageWeight = packageWeight;
		this.things = things;
	}

	public Integer getPackageWeight() {
		return packageWeight;
	}

	public void setPackageWeight(Integer packageWeight) {
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
