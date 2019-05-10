package com.mobiquityinc.parser;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.Constraints;
import com.mobiquityinc.packer.model.TestCase;
import com.mobiquityinc.packer.model.Thing;

/**
 * Parses lines into instances of TestCase instances.
 * @author cezar.carneiro
 */
public class TestCaseParser {

	static private Pattern linePattern = Pattern.compile("^(?<packageWeight>\\d+) :(?<things>( \\((.*?)\\))*)");
	static private Pattern thingPattern = Pattern
			.compile("^\\((?<index>\\d+),(?<weight>\\d+(\\.\\d+)*?),\\D(?<cost>\\d+(\\.\\d+)*?)\\)");

	/**
	 * 
	 * @param line Text in the following format: <br/>
	 * 1 : (1,2,€3)
	 * @return And instance of TestCase
	 */
	public TestCase parse(String line) {
		if(line == null) {
			throw new IllegalArgumentException();
		}
		
		Matcher lineMatcher = linePattern.matcher(line);

		if (lineMatcher.matches()) {
			String packageWeightStr = lineMatcher.group("packageWeight");
			BigDecimal packageWeight = new BigDecimal(packageWeightStr);

			Constraints.check(packageWeight, Constraints.MAX_PACKAGE_WEIGHT,
					String.format("Package weight (%s) above maximum allowed package weight (%s)", packageWeight,
							Constraints.MAX_PACKAGE_WEIGHT));

			String thingsStr = lineMatcher.group("things").trim();
			List<Thing> list = parseThings(thingsStr);

			Constraints.check(list.size(), Constraints.MAX_THINGS,
					String.format("Number of things (%s) above maximum allowed number of thingst (%s)", list.size(),
							Constraints.MAX_THINGS));

			TestCase testCase = new TestCase(packageWeight, list);
			return testCase;
		}
		throw new APIException(String.format("A line with an invalid format was found: %s", line));
	}

	private List<Thing> parseThings(String things) {
		return Stream.of(things.split(" ")).map(thingString -> parseThing(thingString)).collect(Collectors.toList());
	}

	private Thing parseThing(String thingString) {
		Matcher matcher = thingPattern.matcher(thingString);
		if (matcher.matches()) {
			String indexStr = matcher.group("index");
			String weightStr = matcher.group("weight");
			String costStr = matcher.group("cost");

			Integer index = Integer.parseInt(indexStr);
			BigDecimal weight = new BigDecimal(weightStr);

			Constraints.check(weight, Constraints.MAX_THING_WEIGHT,
					String.format("Weight (%s) above maximum weight (%s)", weight, Constraints.MAX_THING_WEIGHT));

			BigDecimal cost = new BigDecimal(costStr);
			Constraints.check(cost, Constraints.MAX_THING_COST,
					String.format("Cost (%s) above maximum cost (%s)", cost, Constraints.MAX_THING_COST));

			return new Thing(index, weight, cost);
		}
		throw new APIException(String.format("Could not parse following item: %s", thingString));
	}

}
