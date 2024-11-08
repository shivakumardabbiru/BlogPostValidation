package Utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ReusableFunctions {
	
	
	/*
	 * This function is to get all the users and read the response
	 * From the response, search for a specific user
	 * once the user is found, get the user id
	 * Return the user id for the calling function 
	 */

	public static int getUserIdByUsername(Response response,String username) {
		
		

		List<Map<String, Object>> users = response.jsonPath().getList("$");
		
		for (Map<String, Object> user : users) {
			if (username.equalsIgnoreCase((String) user.get("username"))) {
				return (int) user.get("id");
			}
		}
		
		throw new RuntimeException("User with username '" + username + "' not found.");
	}

	/*
	 * This function is to get all the Posts for a specific user
	 * This function takes the UserId from the previous call as an input
	 * once the user posts are retrieved using the user Id , it will return the
	 * same to the calling function
	 */
	
	
	public static List<Integer> getPostIdsForUser(Response response, int userId) {
		
		List<Map<String, Object>> posts = response.jsonPath().getList("$");
		List<Integer> postIds = new ArrayList<>();

		for (Map<String, Object> post : posts) {
			if ((int) post.get("userId") == userId) {
				postIds.add((int) post.get("id"));
			}
		}
		return postIds;
	}
	
	
	/*
	 * This function is used to validate the email format #
	 * This will return a boolean value of true or false based on the validation
	 */

	public static boolean isValidEmail(String email) {
		final String emailRegexRFC5322 = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*"
				                       + "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return Pattern.compile(emailRegexRFC5322).matcher(email).matches();
	}
	
	/*
	 * This function is a reusable function to call the post methods .
	 * It takes the endpoint, payload and header data as input 
	 * It provides the post response as a return object to the calling function
	 */
	
	public static Response performPost(String endpoint, String payload, Map<String,String>headers) {
		
		return RestAssured.given()
		           .baseUri(endpoint)
		           .headers(headers)
		           .contentType(ContentType.JSON)
                   .body(payload)
                   .post()
                   .then().log().all().extract().response();	
	}

	
       public static String createReportwithDateTime() {
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
		LocalDateTime localTime = LocalDateTime.now();
		String formattedTime = dateTimeFormatter.format(localTime);
		String reportName = "Test Report" + formattedTime + ".html";
		return reportName;
		
		
	}
	
}
