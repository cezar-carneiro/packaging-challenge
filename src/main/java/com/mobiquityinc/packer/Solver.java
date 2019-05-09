package com.mobiquityinc.packer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mobiquityinc.packer.model.TestCase;
import com.mobiquityinc.packer.model.Thing;
import com.mobiquityinc.util.Numbers;

/**
 * https://www.youtube.com/watch?v=F-dudDe4ugs
 * https://en.wikipedia.org/wiki/Knapsack_problem#0/1_knapsack_problem
 * 
 * @author cezar.carneiro
 *
 */
public class Solver {
	
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
		sortByWeight(things);
		Integer capacity = Numbers.moveFloatingPoint(testCase.getPackageWeight());
		
		int[][] matrix = initializeMatrix(capacity, things.size());

		populateMatrix(capacity, things, matrix);

		return pickResult(capacity, things, matrix);
	}
	
	/**
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
	 * @param capacity Max weight in the package
	 * @param things List of things being evaluated
	 * @param matrix Populated Matrix
	 */
	private void populateMatrix(Integer capacity, List<Thing> things, int[][] matrix) {
		// we iterate on items
		for (int i = 1; i <= things.size(); i++) {
			// we iterate on each capacity
			for (int j = 0; j <= capacity; j++) {
				int weight = Numbers.moveFloatingPoint(things.get(i - 1).getWeight());
				int cost = Numbers.moveFloatingPoint(things.get(i - 1).getCost());
				
				if (weight > j) {
					matrix[i][j] = matrix[i - 1][j];
				} else {
					// we maximize value at this rank in the matrix
					matrix[i][j] = Math.max(matrix[i-1][j], matrix[i-1][j - weight] + cost);
				}
			}
		}
	}

	/**
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
				// we remove items value and weight
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
