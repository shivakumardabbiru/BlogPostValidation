package Utilities;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader {
	
	private static ObjectMapper objmapper = new ObjectMapper();
	
	public static Map<String, String> readJson(String jsonFilePath) throws StreamReadException, DatabindException, IOException {
		
		String jsonPathToRead = System.getProperty("user.dir") + "/src/test/resources/" + jsonFilePath;
		Map<String,String> data = objmapper.readValue(new File(jsonPathToRead), new TypeReference<Map<String,String>>() {});
		return data;
		
	}

}
