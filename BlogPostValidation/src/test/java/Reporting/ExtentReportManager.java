package Reporting;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import Utilities.ReusableFunctions;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;



public class ExtentReportManager implements ITestListener {

    private static ExtentReports extentReports;
    private static ExtentTest test;

    // This method is invoked before any test starts
    @Override
    public void onStart(ITestContext context) {
        // Initialize Extent Reports with the Spark Reporter
    	String fileName = ReusableFunctions.createReportwithDateTime();
    	String fullReportPath = System.getProperty("user.dir") + "\\Reports\\" + fileName;
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(fullReportPath);
        
        sparkReporter.config().setDocumentTitle("API Test Report");
        sparkReporter.config().setReportName("API Testing Results");
        sparkReporter.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.DARK);

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("Environment", "QA");
        extentReports.setSystemInfo("Tester", "Your Name");
    }

    // This method is invoked when a test starts
    @Override
    public void onTestStart(ITestResult result) {
        // Create a new test node for each test method
        test = extentReports.createTest(result.getMethod().getMethodName());
        test.info("Starting test: " + result.getMethod().getMethodName());
    }

    // This method is invoked when a test passes
    @Override
    public void onTestSuccess(ITestResult result) {
        test.pass("Test Passed");
    }

    // This method is invoked when a test fails
    @Override
    public void onTestFailure(ITestResult result) {
        test.fail("Test Failed: " + result.getThrowable());
    }

    // This method is invoked when a test is skipped
    @Override
    public void onTestSkipped(ITestResult result) {
        test.skip("Test Skipped: " + result.getThrowable());
    }

    // This method is invoked after all tests have run
    @Override
    public void onFinish(ITestContext context) {
        if (extentReports != null) {
            extentReports.flush(); // Generate the report
        }
    }
    
    // Utility method to log request details
    public static void logRequest(QueryableRequestSpecification qrb) {
        if (test != null) {
            test.info("Request Details:");
            test.info("Endpoint: " + qrb.getBaseUri());
            test.info("Request Method: " + qrb.getMethod());
            test.info("Request Headers: " + qrb.getHeaders());
            test.info("Request Body: " + (qrb.getBody() != null ? qrb.getBody().toString() : "No body"));
        }
    }

    // Utility method to log response details
    public static void logResponse(Response response, String endpoint) {
        if (test != null) {
            test.info("Status Code: " + response.getStatusCode());
            test.info("Response Headers: " + response.getHeaders().asList().toString());
            test.info("Response Body (Formatted JSON): " + response.asPrettyString());
        }
    }
}