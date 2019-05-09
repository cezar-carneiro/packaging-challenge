package com.mobiquityinc.packer;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.mobiquityinc.packer.model.TestCase;
import com.mobiquityinc.packer.model.Thing;

public class SolverTest {

	/*
	 *  
	    5 : (1,6,€10) none of single item #
		5 : (1,6,€10) (2,6,€10) none of multiple invalid items #
		
		5 : (1,4,€10) single item below limit # 
		5 : (1,5,€10) single item equal to limit #

		5 : (1,5,€10) (2,6,€10) single item from one ok and the other not #
		5 : (1,2,€10) (2,2,€10) (3,6,€10) (4,6,€10) multiple items from multiple ok and multiple not #
		
		6 : (1,1,€10) (2,2,€10) (3,3,€10) all items fit #
		
	    5 : (1,4,€10) (2,3,€10) 2 same value, less weight last #
		5 : (1,3,€10) (2,4,€10) 1 same value, less weight first #
		5 : (1,5,€9) (2,5,€10) 2 same weight, more valuable last #
		5 : (1,5,€10) (2,5,€9) 1 same weight, more valuable first #
		
		5 : (1,4,€4) (2,3,€3) 1 should prioritize heavier but more valuable item over lighter and less valuable #
		6 : (1,6,€10) (2,3,€10) (3,3,€10) 2,3 should prioritize combination of items with more value over same weight and less value single item #
		6 : (1,2,€10) (2,2,€10) (3,5,€20) 1,2 should prioritize combination of items that weight less over single item that weights more #
		
	 */
	
	public static Stream<Arguments> dataSet() {
		return Stream.of(
				Arguments.of(
						"Should return an empty list for a test case with no items",
						/* expected */
						Collections.emptyList(),
						/* param */
						testCase("1", Collections.emptyList())),
				/***************************************************************************/
				Arguments.of(
						"Should return an empty list for a test case with only one item that is above the package limit",
						/* expected */
						Collections.emptyList(),
						/* param */
						testCase("5", listOfThings(thing(1, "6","10")))),
				/***************************************************************************/
				Arguments.of(
						"Should return an empty list for a test case with multiple items above the package limit",
						/* expected */
						Collections.emptyList(),
						/* param */
						testCase("5", listOfThings(thing(1, "6", "10"), thing(2, "6", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick one item out of one item below package limit",
						/* expected */
						listOfThings(thing(1)),
						/* param */
						testCase("5", listOfThings(thing(1, "4", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick one item out of one item that weights exactly the package limit",
						/* expected */
						listOfThings(thing(1)),
						/* param */
						testCase("5", listOfThings(thing(1, "4", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick one item if the test case has one item that is in the package limit and another item that is above the package limit",
						/* expected */
						listOfThings(thing(2)),
						/* param */
						testCase("5", listOfThings(thing(1, "6", "10"), thing(2, "5", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick multiple items if the test case has multiple items below the limit and multiple items above the limit",
						/* expected */
						listOfThings(thing(1), thing(2)),
						/* param */
						testCase("5", listOfThings(thing(1, "2", "10"), thing(2, "2", "10"), thing(3, "6", "10"), thing(4, "6", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick all items if all items fit together in the package",
						/* expected */
						listOfThings(thing(1), thing(2), thing(3)),
						/* param */
						testCase("6", listOfThings(thing(1, "1", "10"), thing(2, "2", "10"), thing(3, "3", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick the lighest item from multiple items with the same value",
						/* expected */
						listOfThings(thing(1)),
						/* param */
						testCase("5", listOfThings(thing(1, "3", "10"), thing(2, "4", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick the lighest item from multiple items with the same value, and the lightest item appears last",
						/* expected */
						listOfThings(thing(2)),
						/* param */
						testCase("5", listOfThings(thing(1, "4", "10"), thing(2, "3", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick the most valuable item from multiple items that have the same weight",
						/* expected */
						listOfThings(thing(2)),
						/* param */
						testCase("5", listOfThings(thing(1, "5", "9"), thing(2, "5", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick the most valuable item from multiple items that have the same weight",
						/* expected */
						listOfThings(thing(1)),
						/* param */
						testCase("5",
								listOfThings(thing(1, "5", "10"), thing(2, "5", "9")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick the most valuable item from multiple items that have the same weight, and the most valuable item appears last",
						/* expected */
						listOfThings(thing(2)),
						/* param */
						testCase("5", listOfThings(thing(1, "5", "9"), thing(2, "5", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick a heavier but more valuable item over a lighter and less valuable item",
						/* expected */
						listOfThings(thing(1)),
						/* param */
						testCase("5", listOfThings(thing(1, "4", "4"), thing(2, "3", "3")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick a combination of items that together have more value over a single item with the same weight but less value",
						/* expected */
						listOfThings(thing(2), thing(3)),
						/* param */
						testCase("6", listOfThings(thing(1, "6", "15"), thing(2, "3", "10"), thing(3, "3", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick a combination of items that together weight less but have the same value of a single item that weights more and have the same value",
						/* expected */
						listOfThings(thing(1), thing(2)),
						/* param */
						testCase("6", listOfThings(thing(1, "2", "10"), thing(2, "2", "10"), thing(3, "5", "20"))))
				);
	}
	
	private Solver solver;
	
	@Before
	@BeforeEach
	public void initialize() {
		solver = new Solver();
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldValidateNullValue() {
		solver.solve(null);
	}
	
	@ParameterizedTest
	@MethodSource("dataSet")
    public void shouldPickItemsFromTestCaseCorrectly(String message, List<Thing> expected, TestCase param) {
		List<Thing> result = solver.solve(param);
        assertTrue(message, expected.equals(result) );
    }
	
	public static Thing thing(Integer index) {
		return thing(index, null, null);
	}
	
	public static Thing thing(Integer index, String weight, String value) {
		return new Thing(index, weight == null ? null : new BigDecimal(weight), value == null ? null : new BigDecimal(value));
	}
	
	public static TestCase testCase(String capacity, List<Thing> things) {
		return new TestCase( new BigDecimal(capacity), things);
	}
	
	public static List<Thing> listOfThings(Thing... t) {
		return Arrays.asList(t);
	}
}
