package com.mobiquityinc.parser;

import static org.junit.Assert.assertEquals;

import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.model.TestCase;
import com.mobiquityinc.test.Instances;

public class TestCaseParserTest {

	private TestCaseParser parser;

	@Before
	@BeforeEach
	public void initialize() {
		parser = new TestCaseParser();
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldValidateNullValue() {
		parser.parse(null);
	}
	
	public static Stream<Arguments> invalidFormat() {
		return Stream.of(
				Arguments.of(""),
				Arguments.of("asd"), 
				Arguments.of("1"), 
				Arguments.of("1  (1,2,€3)"), 
				Arguments.of(" (1,2,€3)"), 
				Arguments.of(" : (1,2,€3)"), 
				Arguments.of("-1 : (1,2,€3)"), 
				Arguments.of("a : (1,2,€3)"),
				Arguments.of("# : (1,2,€3)"),
				Arguments.of("1 : ( ,2,€3)"), 
				Arguments.of("1 : (a,2,€3)"), 
				Arguments.of("1 : (-1,2,€3)"),
				Arguments.of("1 : (#,2,€3)"),
				Arguments.of("1 : (1, ,€3)"), 
				Arguments.of("1 : (1,a,€3)"), 
				Arguments.of("1 : (1,-1,€3)"),
				Arguments.of("1 : (1,#,€3)"),
				Arguments.of("1 : (1,2,)"), 
				Arguments.of("1 : (1,2,€)"), 
				Arguments.of("1 : (1,2,a)"),
				Arguments.of("1 : (1,2,€a)"),

				// constraints
				Arguments.of("101 : (1,2,€a)"),
				Arguments.of("1 : (1,101,€a)"),
				Arguments.of("1 : (1,2,€101)"),
				Arguments.of("1 : (1,2,€3) (2,2,€3) (3,2,€3) (4,2,€3) (5,2,€3) (6,2,€3) "
						+ "(7,2,€3) (8,2,€3) (9,2,€3) (10,2,€3) (11,2,€3) (12,2,€3) "
						+ "(13,2,€3) (14,2,€3) (15,2,€3) (16,2,€3)")
				);
	}
	@ParameterizedTest
	@MethodSource("invalidFormat")
	public void shouldValidateThrowExceptionForInvalidFormat(String param) {
		Assertions.assertThrows(APIException.class, () -> {
			parser.parse(param);
		});
	}
	
	public static Stream<Arguments> validFormat() {
		return Stream.of(
				Arguments.of("1 : (1,2,€3)",
						Instances.testCase("1", Instances.listOfThings(Instances.thing(1, "2", "3")))),
				Arguments.of("1 : (1,2,€3) (4,5,€6)",
						Instances.testCase("1", Instances.listOfThings(Instances.thing(1, "2", "3"), Instances.thing(4, "5", "6")))),
				Arguments.of("1 : (1,2,€3) (4,5,€6) (7,8,€9)",
						Instances.testCase("1", Instances.listOfThings(Instances.thing(1, "2", "3"), Instances.thing(4, "5", "6"),  Instances.thing(7, "8", "9"))))
				);
	}
	
	@ParameterizedTest
	@MethodSource("validFormat")
    public void shouldParseTestCaseLinesCorrectly(String param, TestCase expected) {
        assertEquals(expected, parser.parse(param));
    }

}
