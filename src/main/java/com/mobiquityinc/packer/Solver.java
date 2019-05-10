package com.mobiquityinc.packer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mobiquityinc.packer.model.TestCase;
import com.mobiquityinc.packer.model.Thing;
import com.mobiquityinc.util.Numbers;

/**
 * Can solve the Knapsack problem. Given a set of items, each with a weight and a value, 
 * we must determine the number of each item to include in a collection so that the total weight 
 * is less than or equal to a given limit and the total value must be maximized.
 * 
 * The approach used is based on dynamic programming, as described in:
 * 
 * https://en.wikipedia.org/wiki/Knapsack_problem#0/1_knapsack_problem
 * https://www.youtube.com/watch?v=F-dudDe4ugs
 * 
 * @author cezar.carneiro
 */
public class Solver {
	
	/**
	 * Analyzes the items from the test case and decides which items should go in the package.
	 * 
	 * @param testCase 
	 * @return List of the things that can be put in the package
	 */
	public List<Thing> solve(TestCase testCase) {
		if (testCase == null) {
			throw new IllegalArgumentException();
		}
		
		if(testCase.getPackageWeight() == null || testCase.getPackageWeight().compareTo(BigDecimal.ZERO) == 0) {
			return Collections.emptyList();
		}
		
		if(testCase.getThings() == null || testCase.getThings().size() == 0) {
			return Collections.emptyList();
		}
		
		List<Thing> things = testCase.getThings();
		
		/* Making the lighter items go first so we can prioritize a lighter item 
		 * over a heavier item in case they have the same weight*/
		sortByWeight(things);
		
		/* Moving floating point so we'll have integers instead of doubles
		 * (Indexes in array can only be integers)*/
		Integer capacity = Numbers.moveFloatingPoint(testCase.getPackageWeight()); 
		
		int[][] matrix = initializeMatrix(capacity, things.size());

		populateMatrix(capacity, things, matrix);

		return pickResult(capacity, things, matrix);
	}
	
	/**
	 * A row number i represents the set of all the items from row 1 to row i
	 * A column number j represents the weight capacity of our package.
	 * 
	 * At row 0, when we have no items to pick from, 
	 * the maximum value that can be stored in any knapsack must be 0. 
	 * 
	 * @param capacity Max weight in the package
	 * @param size Number of things to be evaluated
	 * @return initialized Matrix
	 */
	private int[][] initializeMatrix(Integer capacity, int size) {
		// we use a matrix to store the max value at each n-th item
		int[][] matrix = new int[size + 1][capacity + 1];

		// first line is initialized to 0
		for (int i = 0; i <= capacity; i++) {
			matrix[0][i] = 0;
		}
		return matrix;
	}
	
	/**
	 * An entry in row i, column j represents the maximum value that can be obtained 
	 * with items 1, 2, 3 … i, in a package that can hold j weight units.
	 * 
	 * @param capacity Max weight in the package
	 * @param things List of things being evaluated
	 * @param matrix Populated Matrix
	 */
	private void populateMatrix(Integer capacity, List<Thing> things, int[][] matrix) {
		// iterate on the items
		for (int i = 1; i <= things.size(); i++) {
			// iterate on each capacity
			for (int j = 0; j <= capacity; j++) {
				int weight = Numbers.moveFloatingPoint(things.get(i - 1).getWeight());
				int cost = Numbers.moveFloatingPoint(things.get(i - 1).getCost());
				
				if (weight > j) {
					matrix[i][j] = matrix[i - 1][j];
				} else {
					matrix[i][j] = Math.max(matrix[i-1][j], matrix[i-1][j - weight] + cost);
				}
			}
		}
	}

	/**
	 * Iterate back on the Matrix discovering which items can go in the package. 
	 * 
	 * @param capacity Max weight in the package
	 * @param things List of things being evaluated
	 * @param matrix Populated Matrix
	 * @return List of selected Things
	 */
	private List<Thing> pickResult(Integer capacity, List<Thing> things, int[][] matrix) {
		int res = matrix[things.size()][capacity];
		int w = capacity;
		List<Thing> itemsSolution = new ArrayList<>();

		for (int i = things.size(); i > 0 && res > 0; i--) {
			if (res != matrix[i - 1][w]) {
				itemsSolution.add(things.get(i - 1));
				// removing items value and weight
				res -= Numbers.moveFloatingPoint(things.get(i - 1).getCost());
				w -= Numbers.moveFloatingPoint(things.get(i - 1).getWeight());
			}
		}

		sortByIndex(itemsSolution);
		
		return itemsSolution;
	}
	
	private void sortByWeight(List<Thing> things) {
		Collections.sort(things, (Thing o1, Thing o2) -> o1.getWeight().compareTo(o2.getWeight()));
	}

	private void sortByIndex(List<Thing> things) {
		Collections.sort(things, (Thing o1, Thing o2) -> o1.getIndex() - o2.getIndex());
	}
}
