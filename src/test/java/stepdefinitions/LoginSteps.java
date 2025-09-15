package stepdefinitions;

import Utils.RoleManager;
import Utils.TestContext;
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
        token = RoleManager.getAccessToken(role);
        context.setAccessToken(token);
        System.out.println("✅ Logged in as " + role + " | Access Token: " + token);
    }

    @Then("I should get a valid access token")
    public void i_should_get_a_valid_access_token() {
        String token = context.getAccessToken();
        assertNotNull(token, "❌ Token should not be null");
        assertFalse(token.isEmpty(), "❌ Token should not be empty");
    }

    @When("I call logout API")
    public void i_call_logout_api() {
        logoutResponse = Authapi.logout(token);
        System.out.println("Logout Response: " + logoutResponse.asString());
    }

    @Then("I should see a successful logout response")
    public void i_should_see_a_successful_logout_response() {
        logoutResponse.then().statusCode(200);
    }
}
