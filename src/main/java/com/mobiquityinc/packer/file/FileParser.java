package com.mobiquityinc.packer.file;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.Constraints;
import com.mobiquityinc.packer.model.TestCase;
import com.mobiquityinc.packer.model.Thing;

public class FileParser {

	static private Pattern linePattern = Pattern.compile("^(?<packageWeight>\\d+) :(?<things>( \\((.*?)\\))*)");
	static private Pattern thingPattern = Pattern.compile("^\\((?<index>\\d+),(?<weight>\\d+(\\.\\d+)*?),\\D(?<cost>\\d+(\\.\\d+)*?)\\)");
	
	public void parse(String absolutePath, Consumer<TestCase> onTestCaseParsed) {
		try (Stream<String> stream = Files.lines(Paths.get(absolutePath))) {
	        stream.forEach(line -> {
                Matcher lineMatcher = linePattern.matcher(line);

                if (lineMatcher.matches()) {
                    String packageWeightStr = lineMatcher.group("packageWeight");
                    BigDecimal packageWeight = new BigDecimal(packageWeightStr);
                    
                    Constraints.check(packageWeight.doubleValue(), Constraints.MAX_PACKAGE_WEIGHT, 
                    		String.format("Package weight (%s) above maximum allowed package weight (%s)", 
                    				packageWeight, Constraints.MAX_PACKAGE_WEIGHT));
                    
                    String thingsStr = lineMatcher.group("things").trim();
                    List<Thing> list = parseThings(thingsStr);
                    
                    Constraints.check(list.size(), Constraints.MAX_THINGS, 
                    		String.format("Number of things (%s) above maximum allowed number of thingst (%s)", 
                        			list.size(), Constraints.MAX_THINGS));
                    
                    TestCase testCase = new TestCase(packageWeight, list);
                    onTestCaseParsed.accept(testCase);
                }
	        });
		} catch (IOException e) {
			throw new APIException(String.format("Error reading the file %s", absolutePath));
		}
	}
	
	private List<Thing> parseThings(String things) {
		return Stream.of(things.split(" "))
                .map(thingString -> parseThing(thingString))
                .collect(Collectors.toList());
	}
	
	private Thing parseThing(String thingString) {
		Matcher matcher = thingPattern.matcher(thingString);
        if (matcher.matches()) {
            String indexStr = matcher.group("index");
            String weightStr = matcher.group("weight");
            String costStr = matcher.group("cost");
            
        	Integer index = Integer.parseInt(indexStr);
        	BigDecimal weight = new BigDecimal(weightStr);
        	
        	Constraints.check(weight.doubleValue(), Constraints.MAX_THING_WEIGHT, 
        			String.format("Weight (%s) above maximum weight (%s)", 
            				weight, Constraints.MAX_THING_WEIGHT));
        	
        	BigDecimal cost = new BigDecimal(costStr);
        	Constraints.check(cost.doubleValue(), Constraints.MAX_THING_COST, 
        			String.format("Cost (%s) above maximum cost (%s)", 
            				cost, Constraints.MAX_THING_COST));
        	
        	return new Thing(index, weight, cost);
        }
        throw new APIException(String.format("Could not parse following item: %s", thingString));
	}

}
