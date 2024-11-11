package BlogPostTests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import Reporting.ExtentReportManager;
import Utilities.InvokeApiHelper;
import Utilities.JsonReader;
import Utilities.ReusableFunctions;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;

public class APITests {

	private int userId;
	List<Integer> postidsForUser;
	private static InvokeApiHelper apiHelper;
	private static String userToSearch;
	private static String userEndpoint;
	private static String postsEndpoint;
	private static String commentsEndpoint;

	@BeforeClass
	public static void setupURL() throws StreamReadException, DatabindException, IOException {

		// here we setup the default URL and API base path to use throughout the tests
		Map<String, Object> data = JsonReader.readJson("Environment/QA/BlogPostEndpoints.json");
		RestAssured.baseURI = (String) data.get("BlogPostBaseUrl");
		userToSearch = (String) data.get("UserName");
		userEndpoint = (String) data.get("userEndpoint");
		postsEndpoint = (String) data.get("postsEndpoint");
		commentsEndpoint = (String) data.get("commentsEndpoint");

		// Initialize the utility class
		apiHelper = new InvokeApiHelper();
	}

	/*
	 * This test is to call the users api and look for the user Delphine once the
	 * user is found, get the user id of the user. This user id is passed to post
	 * API to get all the posts for user Delphine
	 */

	@Test(priority = 1)
	public void findUser() {

		Response response = apiHelper.sendGetRequest(userEndpoint, null);

		response.then().log().all().extract().response();

		QueryableRequestSpecification queryableSpec = apiHelper.getQueryableSpec();

		// Log request and response to the Extent Report
		ExtentReportManager.logRequest(queryableSpec,userEndpoint);

		ExtentReportManager.logResponse(response );

		Assert.assertEquals(response.statusCode(), 200, "Expected status code 200.");

		// Get the user id of the user Delphine.
		userId = ReusableFunctions.getUserIdByUsername(response, userToSearch);

		// Handle potential null values with Optional
		Optional<String> optionalUserID = Optional.ofNullable(String.valueOf(userId));
		Assert.assertTrue(optionalUserID.isPresent(), "The field 'UserID' is missing or null");

	}

	/*
	 * This test method is to call the post API using the user id of Delphine Once
	 * the call is made, retrieve all the post Id's of the user This test method
	 * will return all the post id's in a list object This list is passed to
	 * comments API to get comments for each post.
	 */

	@Test(priority = 2, dependsOnMethods = "findUser")
	public void getPostsForUser() {

		Response response = apiHelper.sendGetRequest(postsEndpoint, null);

		QueryableRequestSpecification queryableSpec = apiHelper.getQueryableSpec();
		// Log request and response to the Extent Report
		ExtentReportManager.logRequest(queryableSpec, postsEndpoint);

		ExtentReportManager.logResponse(response);

		Assert.assertEquals(response.statusCode(), 200, "Expected status code 200.");

		// Call the getPostIDs method to retrieve all the posts for user Delphine

		postidsForUser = ReusableFunctions.getPostIdsForUser(response, userId);

		Assert.assertFalse(postidsForUser.isEmpty(), "No posts found for user with ID " + userId);
	}

	/*
	 * This test method is to call the comments API and validate the email in each
	 * comment This method takes the post id as query param and gets all the
	 * comments for each post Once the list of comments for each post are retrieved,
	 * it will validate the email Any invalid email will be asserted and logged in
	 * the report.
	 */

	@Test(priority = 3, dependsOnMethods = "getPostsForUser")
	public void validateUserPostCommentsEmails() {

		for (int postId : postidsForUser) {

			// Prepare query parameters
			Map<String, Object> queryParams = new HashMap<>();
			queryParams.put("postId", postId);

			Response response = apiHelper.sendGetRequest(commentsEndpoint, queryParams);

			System.out.println("postid " + postId + ": " + response.asPrettyString());

			QueryableRequestSpecification queryableSpec = apiHelper.getQueryableSpec();
			// Log request and response to the Extent Report
			ExtentReportManager.logRequest(queryableSpec, commentsEndpoint);

			ExtentReportManager.logResponse(response);

			Assert.assertEquals(response.statusCode(), 200, "Expected status code 200.");

			// Log email validation results in a table format to the Extent Report
			ExtentReportManager.validateEmailsWithReport(response);

		}
	}
}
