package com.mobiquityinc;

import com.mobiquityinc.packer.Packer;

public class App {
	
    public static void main( String[] args ) {
        if (args.length == 0) {
            System.out.println("Missing argument.\nProper Usage is: java -jar <jarname> <file>");
            System.exit(0);
        }
    	
        String solutionResult = Packer.pack(args[args.length - 1]);
        System.out.print(solutionResult);
    }
}
