package Reporting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {
	
	public static ExtentReports extentReports;
	
	public static ExtentReports createInstance(String fileName, String ReportName, String DocumentTitle ) {
		
		ExtentSparkReporter extentSparkReport = new ExtentSparkReporter(fileName);
		extentSparkReport.config().setReportName(ReportName);
		extentSparkReport.config().setDocumentTitle(DocumentTitle);
		extentSparkReport.config().setTheme(Theme.DARK);
		extentSparkReport.config().setEncoding("utf-8");
		extentReports = new ExtentReports();
		extentReports.attachReporter(extentSparkReport);
		return extentReports;
		
	}
	
	public static String createReportwithDateTime() {
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
		LocalDateTime localTime = LocalDateTime.now();
		String formattedTime = dateTimeFormatter.format(localTime);
		String reportName = "Test Report" + formattedTime + ".html";
		return reportName;
		
		
	}
	
	public static void logPassDetails(String log) {
		
		ReportSetup.extentTest.get().pass(MarkupHelper.createLabel(log,ExtentColor.GREEN));
		
	}
	
	public static void logFailDetails(String log) {
		
		ReportSetup.extentTest.get().fail(MarkupHelper.createLabel(log,ExtentColor.RED));
		
	}
	
	public static void logInfoDetails(String log) {
		
		ReportSetup.extentTest.get().info(MarkupHelper.createLabel(log,ExtentColor.GREY));
		
	}
	
	public static void logWarningDetails(String log) {
		
		ReportSetup.extentTest.get().warning(MarkupHelper.createLabel(log,ExtentColor.YELLOW));
		
	}
}

