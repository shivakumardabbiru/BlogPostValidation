package Utilities;

import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;

public class InvokeApiHelper {

	private static RequestSpecification requestSpec;

	// Constructor to initialize the base request specification
	public InvokeApiHelper() {

		requestSpec = RestAssured.given().contentType("application/json").header("Accept", "application/json");
	}

	// Method to get the QueryableRequestSpecification for debugging/verification

	public QueryableRequestSpecification getQueryableSpec() {
		return SpecificationQuerier.query(requestSpec);
	}

	// Reusable method for POST requests
	public static Response performPost(String endpoint, String payload, Map<String, String> headers) {

		return RestAssured.given().baseUri(endpoint).headers(headers).contentType(ContentType.JSON).body(payload).post()
				.then().log().all().extract().response();
	}

	// Reusable method for GET requests
	public Response sendGetRequest(String endpoint, Map<String, Object> queryParams) {

		requestSpec = RestAssured.given();

		// Add query parameters if provided
		if (queryParams != null && !queryParams.isEmpty()) {
			requestSpec.queryParams(queryParams);
		}

		return requestSpec.get(endpoint);

	}

}
