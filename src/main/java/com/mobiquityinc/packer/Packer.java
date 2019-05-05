package com.mobiquityinc.packer;

import java.util.List;

import com.mobiquityinc.packer.file.FileParser;
import com.mobiquityinc.packer.model.TestCase;
import com.mobiquityinc.packer.model.Thing;

public class Packer {

	public static String pack(String filePath) {
		FileParser fp = new FileParser();
		Solver solver = new Solver();
		SolutionWriter writer = new SolutionWriter();
		
		fp.parse(filePath, (TestCase testCase) -> {
//			System.out.println(testCase);
			List<Thing> solution = solver.solve(testCase);
			writer.write(solution);
		});
		
		return writer.toString();
	}
}
