package com.mobiquityinc.packer;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class PackerTest {
	
    @Test
    public void shouldComputeOficialTestCasesCorrectly() throws URISyntaxException {
		URI uri = getClass().getResource("/oficial-tests.txt").toURI();
		Path path = Paths.get(uri);
		File file = path.toFile();
		
		String result = Packer.pack(file.getAbsolutePath());
		String expected = 
				"4\n" +
				"-\n" + 
				"2,7\n" + 
				"8,9\n";
		
		assertEquals("Should compute the oficial test cases provided my Mobiquity correctly.", expected, result);
    }
}
