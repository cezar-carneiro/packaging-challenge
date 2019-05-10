package com.mobiquityinc;

import com.mobiquityinc.packer.Packer;

public class App {
	
    public static void main( String[] args ) {
        if (args.length == 0) {
            System.out.println("Missing argument.\nProper Usage is: java program <filepath>");
            System.exit(0);
        }
    	
        String solutionResult = Packer.pack(args[0]);
        System.out.print(solutionResult);
    }
}
