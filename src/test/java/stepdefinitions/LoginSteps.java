package stepdefinitions;

import Utils.ExtentManager;
import Utils.RoleManager;
import Utils.TestContext;
import com.aventstack.extentreports.ExtentTest;
import endPoints.Authapi;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertFalse;

public class LoginSteps {

    private String token;
    private Response logoutResponse;
    private final TestContext context;

    // PicoContainer will inject TestContext here
    public LoginSteps(TestContext context) {
        this.context = context;
    }

    @Given("I login as {string}")
    public void i_login_as_role(String role) {

        ExtentTest test = ExtentManager.createTest("Login as role: " + role);
        token = RoleManager.getAccessToken(role);
        context.setAccessToken(token);

        if (token != null && !token.isEmpty())
        {
            test.pass("✅ Logged in successfully as " + role);
            test.pass("Access Token: " + token);
        }
        else
        {
            test.fail("❌ Failed to log in as " + role + ". Token is null or empty.");
        }

        System.out.println("✅ Logged in as " + role + " | Access Token: " + token);

    }

    @Then("I should get a valid access token")
    public void i_should_get_a_valid_access_token() {
        ExtentTest test = ExtentManager.createTest("Validate Access Token");

        String token = context.getAccessToken();
        try {
            assertNotNull(token, "❌ Token should not be null");
            assertFalse(token.isEmpty(), "❌ Token should not be empty");
            test.pass("✅ Token is valid and not empty.");
        } catch (AssertionError e) {
            test.fail("❌ Invalid token: " + e.getMessage());
            throw e;
        }
    }

    @When("I call logout API")
    public void i_call_logout_api() {
        ExtentTest test = ExtentManager.createTest("Call Logout API");

        logoutResponse = Authapi.logout(token);

        int statusCode = logoutResponse.getStatusCode();
        String body = logoutResponse.asString();

        if (statusCode == 200) {
            test.pass("✅ Logout API called successfully. Status: " + statusCode);
        } else {
            test.fail("❌ Logout API failed. Status: " + statusCode);
        }

        test.pass("Response Body: " + body);
        System.out.println("Logout Response: " + body);
    }

    @Then("I should see a successful logout response")
    public void i_should_see_a_successful_logout_response() {
        logoutResponse.then().statusCode(200);
    }
}
