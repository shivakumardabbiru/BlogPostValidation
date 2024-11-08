package BlogPostTests;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import Reporting.ExtentReportManager;
import Utilities.JsonReader;
import Utilities.ReusableFunctions;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;


public class APITests {
	

	private int userId;
	List<Integer> postidsForUser;

	@BeforeClass
	public static void setupURL() throws StreamReadException, DatabindException, IOException {
		// here we setup the default URL and API base path to use throughout the tests
		Map<String, Object> data = JsonReader.readJson("Environment/QA/BlogPostEndpoints.json");
		RestAssured.baseURI = (String) data.get("BlogPostBaseUrl");
	}

	@Test(priority = 1)
	public void findUser() {
		
		RequestSpecification request = RestAssured.given();
		
		                     request.log().all();
		
		Response response = request.get("/users")
				                   .then().log().all().extract().response();
		
		QueryableRequestSpecification qrb = SpecificationQuerier.query(request);
		
		 // Log request and response to the Extent Report
        ExtentReportManager.logRequest(qrb);
        ExtentReportManager.logResponse(response, "GET /users");

		Assert.assertEquals(response.statusCode(), 200, "Expected status code 200.");
		
		userId = ReusableFunctions.getUserIdByUsername(response, "Delphine");
		System.out.println("User found: ID = " + userId);
	}

	@Test(priority = 2, dependsOnMethods = "findUser")
	public void getPostsForUser() {
		
		RequestSpecification request = RestAssured.given();
		                     request.log().all();
		                     
		                 Response response = request.get("/posts");

        Assert.assertEquals(response.statusCode(), 200, "Expected status code 200.");
        
		postidsForUser = ReusableFunctions.getPostIdsForUser(response, userId);
		Assert.assertFalse(postidsForUser.isEmpty(), "No posts found for user with ID " + userId);
	}

	@Test(priority = 3, dependsOnMethods = "getPostsForUser")
	public void validateUserPostCommentsEmails() {
		for (int postId : postidsForUser) {
			Response response = RestAssured.given().queryParam("postId", postId).get("/comments");

			Assert.assertEquals(response.statusCode(), 200, "Expected status code 200.");

			List<Map<String, Object>> comments = response.jsonPath().getList("$");

			for (Map<String, Object> comment : comments) {
				String commentEmail = (String) comment.get("email");
				Assert.assertTrue(ReusableFunctions.isValidEmail(commentEmail), "Invalid email: " + commentEmail);
				System.out.println(commentEmail + " is a valid email? " + ReusableFunctions.isValidEmail(commentEmail));
			}
		}
	}

}
