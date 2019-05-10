package com.mobiquityinc.file;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.file.TestCaseFileReader;

public class TestCaseFileReaderTest {
	
	static private Consumer<String> dummyLamba = (String line) -> {};

	private TestCaseFileReader reader;
	
	@Before
	@BeforeEach
	public void initialize() {
		reader = new TestCaseFileReader();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldValidateFirstParameterNull() {
		reader.read(null, dummyLamba);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldValidateSecondParameterNull() {
		reader.read("", null);
	}
	
	@Test(expected = APIException.class)
	public void shouldValidateFileThatDoesntExist() {
		reader.read("unexistant.txt", dummyLamba);
	}
	
	@Test
	public void shouldReadEmptyFileCorrectly() throws URISyntaxException {
		URI uri = getClass().getResource("/empty.txt").toURI();
		Path path = Paths.get(uri);
		File file = path.toFile();
		Counter counter = new Counter();
		reader.read(file.getAbsolutePath(), (String line) -> {
			counter.increment();
		});
		assertEquals("Should read a empty file correctly.", 0, counter.getCount());
	}
	
	@Test
	public void shouldReadSingleLineFileCorrectly() throws URISyntaxException {
		URI uri = getClass().getResource("/single-line.txt").toURI();
		Path path = Paths.get(uri);
		File file = path.toFile();
		Counter counter = new Counter();
		reader.read(file.getAbsolutePath(), (String line) -> {
			if(line != null && line.length() > 0) {
				counter.increment();
			}
		});
		assertEquals("Should read a single line.", 1, counter.getCount());
	}
	
	@Test
	public void shouldReadMultipleLinesFileCorrectly() throws URISyntaxException {
		URI uri = getClass().getResource("/multiple-lines.txt").toURI();
		Path path = Paths.get(uri);
		File file = path.toFile();
		Counter counter = new Counter();
		reader.read(file.getAbsolutePath(), (String line) -> {
			if(line != null && line.length() > 0) {
				counter.increment();
			}
		});
		assertEquals("Should read a multiple lines.", 3, counter.getCount());
	}
	
	class Counter {
		private int count = 0;
		void increment() { count++; }
		int getCount() { return count; }
	}
	
}
