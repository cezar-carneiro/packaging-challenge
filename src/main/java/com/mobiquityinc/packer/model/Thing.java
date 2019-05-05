package com.mobiquityinc.packer.model;

import java.math.BigDecimal;

public class Thing {
	
	private Integer index;
	private BigDecimal weight; 
	private BigDecimal cost;
	
	public Thing(Integer index, BigDecimal weight, BigDecimal cost) {
		this.index = index;
		this.weight = weight;
		this.cost = cost;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "Thing [index=" + index + ", weight=" + weight + ", cost=" + cost + "]";
	} 
	
}
