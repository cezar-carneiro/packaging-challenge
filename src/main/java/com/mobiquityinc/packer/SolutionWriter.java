package com.mobiquityinc.packer;

import java.util.List;

import com.mobiquityinc.packer.model.Thing;

/**
 * Class that can accumulate the result as text
 * @author cezar.carneiro
 *
 */
public class SolutionWriter {
	
	private StringBuilder stringBuilder = new StringBuilder();
	
	/**
	 * 
	 * @param things List of things to format as text
	 */
	public void write(List<Thing> things) {
		if(things == null || things.size() == 0) {
			stringBuilder.append("-\n");// write dash
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
