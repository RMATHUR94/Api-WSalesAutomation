package runner;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/resources/features/CompanyList.feature",   // path to feature files
        glue = {"stepdefinitions" , "hooks"},   // package with step defs tags = {"@Customerlist"}
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html", // simple HTML report
                "json:target/cucumber-report/cucumber.json",  // JSON file for Maven report plugin
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true
//        tags = "@smoke"    // optional, run only scenarios with @smoke tag
)
public class TestRunner {
}
