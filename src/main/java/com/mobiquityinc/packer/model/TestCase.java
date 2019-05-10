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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((packageWeight == null) ? 0 : packageWeight.hashCode());
		result = prime * result + ((things == null) ? 0 : things.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestCase other = (TestCase) obj;
		if (packageWeight == null) {
			if (other.packageWeight != null)
				return false;
		} else if (!packageWeight.equals(other.packageWeight))
			return false;
		if (things == null) {
			if (other.things != null)
				return false;
		} else if (!things.equals(other.things))
			return false;
		return true;
	}
	
}
