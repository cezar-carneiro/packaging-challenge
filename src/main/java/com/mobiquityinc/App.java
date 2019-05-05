package com.mobiquityinc;

import com.mobiquityinc.packer.Packer;

public class App {
	
    public static void main( String[] args ) {
    	//TODO: validate arguments
//    	for(String arg: args)
//    		System.out.println(arg	);
    	
        String solutionResult = Packer.pack(args[0]);
        System.out.print(solutionResult);
    	
    }
}
