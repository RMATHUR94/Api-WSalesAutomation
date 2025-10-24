package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import Utils.ExtentManager;

public class ExtentHooks {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ExtentTest test;

    @Before
    public void beforeScenario(Scenario scenario) {
        test = extent.createTest(scenario.getName());
        test.log(Status.INFO, "Starting scenario: " + scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            test.log(Status.FAIL, "Scenario Failed: " + scenario.getName());
            test.log(Status.FAIL, scenario.getStatus().toString());
        } else {
            test.log(Status.PASS, "Scenario Passed: " + scenario.getName());
        }
        extent.flush(); // ✅ generate report at the end
    }

}
