package BlogPostTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Utilities.ExtentReportManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class APITests extends ExtentReportManager {

	private int userId;
	List<Integer> postidsForUserId;

	@BeforeClass
	public static void setupURL() {
		// here we setup the default URL and API base path to use throughout the tests
		RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
	}

	@Test(priority = 1)

	void FindUser() {

		// Perform GET request to the endpoint

		Response response = RestAssured.given().contentType(ContentType.JSON).get("/users");

		// Check if the response status code is 200
		if (response.statusCode() == 200) {
			// Parse the JSON response as a List of Maps
			List<Map<String, Object>> users = response.jsonPath().getList("$");

			// Iterate through the list and print user details
			for (Map<String, Object> user : users) {

				String userName = (String) user.get("username");

				if (userName.equalsIgnoreCase("Delphine")) {
					userId = (int) user.get("id");
					System.out.println("ID: " + userId);
					break;
				}

			}

		} else {
			System.out.println("Request failed with status code: " + response.statusCode());
		}
	}

	@Test(priority = 2, dependsOnMethods = "FindUser")

	void getPostsforuser() {

		Response response = RestAssured.given().contentType(ContentType.JSON).get("/posts");

		if (response.statusCode() == 200) {

			List<Map<String, Object>> items = response.jsonPath().getList("$");
			postidsForUserId = new ArrayList<>();

			for (Map<String, Object> item : items) {
				if ((int) item.get("userId") == userId) {
					postidsForUserId.add((int) item.get("id"));
				}
			}

		} else {

			System.out.println("Request failed with status code: " + response.statusCode());
		}

	}

	@Test(priority = 3, dependsOnMethods = "getPostsforuser")
	void getUserPostcomments() {

		final String EMAIL_REGEX_RFC5322 = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*"
				+ "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

		for (int postId : postidsForUserId) {

			// Add a single query parameter to the request
			Response response = RestAssured.given().queryParam("postId", postId) // Adding query parameter ?postID
					.when().get("/comments");

			if (response.statusCode() == 200) {

				List<Map<String, Object>> comments = response.jsonPath().getList("$");

				// Iterate through the list and print user details
				for (Map<String, Object> comment : comments) {

					String commenteMail = (String) comment.get("email");
					Pattern pattern = Pattern.compile(EMAIL_REGEX_RFC5322);

					Assert.assertEquals(pattern.matcher(commenteMail).matches(), true);

					System.out.println(commenteMail + " is a valid Email? :" + pattern.matcher(commenteMail).matches());

				}
			} else {
				System.out.println("Request failed with status code: " + response.statusCode());
			}
		}
	}
}
