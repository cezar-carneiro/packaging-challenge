package com.mobiquityinc.packer;

import java.util.List;

import com.mobiquityinc.packer.file.TestCaseFileReader;
import com.mobiquityinc.packer.model.TestCase;
import com.mobiquityinc.packer.model.Thing;
import com.mobiquityinc.parser.TestCaseParser;

public class Packer {

	/**
	 * Takes the absolute path to a file containing test cases for the Knapsack Problem, one test case per line. 
	 * Reads the file. Processes the test cases and returns a string with the solution
	 * for all the test cases.
	 * 
	 * One test case solution per line.<br/>
	 * Ex.:<br/>
	 * 4<br/>
	 * -<br/>
	 * 2,7<br/>
	 * 8,9<br/>
	 * @param absoluteFilePath for a file containing multiple test cases
	 * @return string with the solution
	 * for all the test cases
	 */
	public static String pack(String absoluteFilePath) {
		TestCaseFileReader reader = new TestCaseFileReader();
		TestCaseParser tcParser = new TestCaseParser();
		Solver solver = new Solver();
		SolutionWriter writer = new SolutionWriter();
		
		reader.read(absoluteFilePath, (String line) -> { 
			// This callback is executed for every line of the file that is read
			TestCase testCase = tcParser.parse(line);
			List<Thing> solution = solver.solve(testCase);
			writer.write(solution); // accumulate the solution for the current test case as text in the 'writer' object
		});
		
		return writer.toString(); // extract all the solutions for every test case from 
	}
}
