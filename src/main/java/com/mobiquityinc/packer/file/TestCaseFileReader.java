package com.mobiquityinc.packer.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.mobiquityinc.exception.APIException;

/**
 * Responsible for reading the lines of a given file.
 * @author cezar.carneiro
 *
 */
public class TestCaseFileReader {

	/**
	 * Tries to read the file under 'abolutePath'. 
	 * Calls 'onLineRead' for every line that was read.
	 * 
	 * @param absolutePath
	 * @param onLineRead
	 */
	public void read(String absolutePath, Consumer<String> onLineRead) {
		if (absolutePath == null || onLineRead == null) {
			throw new IllegalArgumentException();
		}
		if(!Files.isReadable(Paths.get(absolutePath))){
			throw new APIException(String.format("The specified file (%s, args) doesn't exist or cannot be read.", absolutePath));
		}
		
		try (Stream<String> stream = Files.lines(Paths.get(absolutePath))) {
	        stream.forEach(line -> {
                    onLineRead.accept(line);
	        });
		} catch (IOException e) {
			throw new APIException(String.format("Error reading the file %s", absolutePath));
		}
	}
	
}
