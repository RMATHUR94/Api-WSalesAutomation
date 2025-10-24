package Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;
    private static ExtentTest test;

    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter reporter = new ExtentSparkReporter("target/ExtentReport.html");
            reporter.config().setReportName("Essex API Automation Report");
            reporter.config().setDocumentTitle("Test Results");
            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }
        return extent;
    }

    public static ExtentTest createTest(String testName) {
        test = getInstance().createTest(testName);
        return test;
    }

    public static ExtentTest getTest() {
        return test;
    }
}
