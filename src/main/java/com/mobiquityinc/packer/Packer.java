package com.mobiquityinc.packer;

import com.mobiquityinc.packer.file.FileParser;
import com.mobiquityinc.packer.model.TestCase;

public class Packer {

	public static String pack(String filePath) {
		FileParser fp = new FileParser(filePath);
		
		fp.parse((TestCase testCase) -> {
			//TODO: call 'solver' for every testCase here
			
			System.out.println(testCase);
		});
		
		return null;
	}
}
