package runner;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/resources/features/CompanyList.feature",   // path to feature files
        glue = {"stepdefinitions"},                // package with step defs
        plugin = {"pretty", "html:target/cucumber-reports.html",
                "json:target/cucumber.json"},
        monochrome = true
//        tags = "@smoke"    // optional, run only scenarios with @smoke tag
)
public class TestRunner {
}
