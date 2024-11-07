package Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ReusableFunctions {

	public static int getUserIdByUsername(String username) {
		
		Response response = RestAssured.given()
				                       .log()
				                       .all()
				                       .contentType(ContentType.JSON)
				                       .get("/users");

		Assert.assertEquals(response.statusCode(), 200, "Expected status code 200.");

		List<Map<String, Object>> users = response.jsonPath().getList("$");
		
		for (Map<String, Object> user : users) {
			if (username.equalsIgnoreCase((String) user.get("username"))) {
				return (int) user.get("id");
			}
		}
		
		throw new RuntimeException("User with username '" + username + "' not found.");
	}

	public static List<Integer> getPostIdsForUser(int userId) {
		
		Response response = RestAssured.given()
				                       .contentType(ContentType.JSON)
				                       .get("/posts");

		Assert.assertEquals(response.statusCode(), 200, "Expected status code 200.");

		List<Map<String, Object>> posts = response.jsonPath().getList("$");
		List<Integer> postIds = new ArrayList<>();

		for (Map<String, Object> post : posts) {
			if ((int) post.get("userId") == userId) {
				postIds.add((int) post.get("id"));
			}
		}
		return postIds;
	}

	public static boolean isValidEmail(String email) {
		final String emailRegexRFC5322 = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*"
				                       + "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return Pattern.compile(emailRegexRFC5322).matcher(email).matches();
	}

}
