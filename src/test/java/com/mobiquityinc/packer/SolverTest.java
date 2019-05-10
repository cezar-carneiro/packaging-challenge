package com.mobiquityinc.packer;

import static org.junit.Assert.assertTrue;

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
import com.mobiquityinc.test.Instances;

public class SolverTest {

	public static Stream<Arguments> dataSet() {
		return Stream.of(
				Arguments.of(
						"Should return an empty list for a test case with no items",
						/* expected */
						Collections.emptyList(),
						/* param */
						Instances.testCase("1", Collections.emptyList())),
				/***************************************************************************/
				Arguments.of(
						"Should return an empty list for a test case with only one item that is above the package limit",
						/* expected */
						Collections.emptyList(),
						/* param */
						Instances.testCase("5", Instances.listOfThings(Instances.thing(1, "6","10")))),
				/***************************************************************************/
				Arguments.of(
						"Should return an empty list for a test case with multiple items above the package limit",
						/* expected */
						Collections.emptyList(),
						/* param */
						Instances.testCase("5", Instances.listOfThings(Instances.thing(1, "6", "10"), Instances.thing(2, "6", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick one item out of one item below package limit",
						/* expected */
						Instances.listOfThings(Instances.thing(1)),
						/* param */
						Instances.testCase("5", Instances.listOfThings(Instances.thing(1, "4", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick one item out of one item that weights exactly the package limit",
						/* expected */
						Instances.listOfThings(Instances.thing(1)),
						/* param */
						Instances.testCase("5", Instances.listOfThings(Instances.thing(1, "4", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick one item if the test case has one item that is in the package limit and another item that is above the package limit",
						/* expected */
						Instances.listOfThings(Instances.thing(2)),
						/* param */
						Instances.testCase("5", Instances.listOfThings(Instances.thing(1, "6", "10"), Instances.thing(2, "5", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick multiple items if the test case has multiple items below the limit and multiple items above the limit",
						/* expected */
						Instances.listOfThings(Instances.thing(1), Instances.thing(2)),
						/* param */
						Instances.testCase("5", Instances.listOfThings(Instances.thing(1, "2", "10"), Instances.thing(2, "2", "10"), Instances.thing(3, "6", "10"), Instances.thing(4, "6", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick all items if all items fit together in the package",
						/* expected */
						Instances.listOfThings(Instances.thing(1), Instances.thing(2), Instances.thing(3)),
						/* param */
						Instances.testCase("6", Instances.listOfThings(Instances.thing(1, "1", "10"), Instances.thing(2, "2", "10"), Instances.thing(3, "3", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick the lighest item from multiple items with the same value",
						/* expected */
						Instances.listOfThings(Instances.thing(1)),
						/* param */
						Instances.testCase("5", Instances.listOfThings(Instances.thing(1, "3", "10"), Instances.thing(2, "4", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick the lighest item from multiple items with the same value, and the lightest item appears last",
						/* expected */
						Instances.listOfThings(Instances.thing(2)),
						/* param */
						Instances.testCase("5", Instances.listOfThings(Instances.thing(1, "4", "10"), Instances.thing(2, "3", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick the most valuable item from multiple items that have the same weight",
						/* expected */
						Instances.listOfThings(Instances.thing(2)),
						/* param */
						Instances.testCase("5", Instances.listOfThings(Instances.thing(1, "5", "9"), Instances.thing(2, "5", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick the most valuable item from multiple items that have the same weight",
						/* expected */
						Instances.listOfThings(Instances.thing(1)),
						/* param */
						Instances.testCase("5",
								Instances.listOfThings(Instances.thing(1, "5", "10"), Instances.thing(2, "5", "9")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick the most valuable item from multiple items that have the same weight, and the most valuable item appears last",
						/* expected */
						Instances.listOfThings(Instances.thing(2)),
						/* param */
						Instances.testCase("5", Instances.listOfThings(Instances.thing(1, "5", "9"), Instances.thing(2, "5", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick a heavier but more valuable item over a lighter and less valuable item",
						/* expected */
						Instances.listOfThings(Instances.thing(1)),
						/* param */
						Instances.testCase("5", Instances.listOfThings(Instances.thing(1, "4", "4"), Instances.thing(2, "3", "3")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick a combination of items that together have more value over a single item with the same weight but less value",
						/* expected */
						Instances.listOfThings(Instances.thing(2), Instances.thing(3)),
						/* param */
						Instances.testCase("6", Instances.listOfThings(Instances.thing(1, "6", "15"), Instances.thing(2, "3", "10"), Instances.thing(3, "3", "10")))),
				/***************************************************************************/
				Arguments.of(
						"Should pick a combination of items that together weight less but have the same value of a single item that weights more and have the same value",
						/* expected */
						Instances.listOfThings(Instances.thing(1), Instances.thing(2)),
						/* param */
						Instances.testCase("6", Instances.listOfThings(Instances.thing(1, "2", "10"), Instances.thing(2, "2", "10"), Instances.thing(3, "5", "20"))))
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
	
}
