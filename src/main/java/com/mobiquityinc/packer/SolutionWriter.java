package com.mobiquityinc.packer;

import java.util.List;

import com.mobiquityinc.packer.model.Thing;

/**
 * Class that can accumulate the result of multiple test cases formatted as text.
 * One test case per line.<br/>
 * Ex.:<br/>
 * 4<br/>
 * -<br/>
 * 2,7<br/>
 * 8,9<br/>
 * 
 * @author cezar.carneiro
 *
 */
public class SolutionWriter {
	
	private StringBuilder stringBuilder = new StringBuilder();
	
	/**
	 * @param things Solution for a test case (list of items that could be picked)
	 */
	public void write(List<Thing> things) {
		if(things == null) {
			throw new IllegalArgumentException();
		}
		if(things.size() == 0) {
			stringBuilder.append("-\n");// write a dash if no items could be picked
			return;
		}
		
		// we already checked that list is not empty, so it will contain at least one element
		stringBuilder.append(things.get(0).getIndex()); // print without comma
		
		for(int i = 1; i < things.size(); i++) { // printing from second element on
			stringBuilder.append("," + things.get(i).getIndex());// print with a comma
		}
		stringBuilder.append("\n");
	}
	
	public String toString() {
		return stringBuilder.toString();
	}

}
