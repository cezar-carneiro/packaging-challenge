package com.mobiquityinc.packer;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.mobiquityinc.packer.model.Thing;

public class SolutionWriterTest {
	
	private SolutionWriter solutionWriter;
	
	@Before
	public void setup() {
		solutionWriter = new SolutionWriter();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullParameter() {
		solutionWriter.write(null);
	}
	
	@Test
	public void testEmptyList() {
		solutionWriter.write(Collections.emptyList());
		String result = solutionWriter.toString();
		assertEquals("Should print a dash (-) for an empty list.", "-\n", result);
	}
	
	@Test
	public void testSingleElement() {
		solutionWriter.write(Arrays.asList(new Thing(1, null, null)));
		String result = solutionWriter.toString();
		assertEquals("Should print the single element index followed by a line break.", "1\n", result);
	}
	
	@Test
	public void testMultipleElements() {
		solutionWriter.write(Arrays.asList(new Thing(1, null, null), new Thing(2, null, null)));
		String result = solutionWriter.toString();
		assertEquals("Should print all the elements indexes separated by a comma followed by a line break.", "1,2\n", result);
	}
	
	@Test
	public void testMultipleLines() {
		solutionWriter.write(Collections.emptyList());
		solutionWriter.write(Arrays.asList(new Thing(1, null, null)));
		solutionWriter.write(Arrays.asList(new Thing(1, null, null), new Thing(2, null, null)));
		String result = solutionWriter.toString();
		assertEquals("Should print all the lines, each line ending with line break.", "-\n1\n1,2\n", result);
	}
	
}
