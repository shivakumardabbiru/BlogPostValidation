package Reporting;

import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;


public class ReportSetup implements ITestListener {
	
	private static ExtentReports extentReports;
	public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
	
	public void onStart(ITestListener Context) {
		
		String fileName = ExtentReportManager.createReportwithDateTime();
		String fullReportPath = System.getProperty("User.dir") + "\\Reports\\" + fileName;
		extentReports = ExtentReportManager.createInstance(fullReportPath, "API Automation Test Report", "Execution Report");
		
	}
	
	public void onFinish(ITestListener Context) {
		
		if (extentReports != null) {
			extentReports.flush();
		}
		
	}
	
	public void onTestStart(ITestResult result ) {
		
		ExtentTest test  = extentReports.createTest("Test Name " + result.getMethod().getMethodName());
		extentTest.set(test);		
		
	}
	
	
	

}
