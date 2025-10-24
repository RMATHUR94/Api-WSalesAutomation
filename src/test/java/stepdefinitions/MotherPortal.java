package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertEquals;

public class MotherPortal {

    // Shared variables for steps
    private Response loginResponse;
    private String token;

    @Given("Logged in with {string} and {string} on mother portal.")
    public void logged_in_with_and_on_mother_portal(String email, String password) {
        // Base URL
        String baseUrl = "https://dev.d35iy77kbiv1w7.amplifyapp.com";
        RestAssured.baseURI = baseUrl;

        // JSON payload using parameters
        String payload = "{\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"password\": \"" + password + "\"\n" +
                "}";

        // Send request with query param 'secret'
        Response loginResponse  = given()
                .header("Content-Type", "application/json")
                .queryParam("secret", "ABDC")
                .body(payload)
                .when()
                .post("/api/swell/revalidate/user/login")  // usually login is POST
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();

        // Optionally, extract token or data for later use
        token = loginResponse.jsonPath().getString("results.user.id"); // adjust path if needed
        System.out.println("Login successful. Token/User ID: " + token);
    }

    @When("getting token and check the response status code should be {int}")
    public void getting_token_and_check_the_response_status_code_should_be(Integer expectedStatusCode) {

        // 1️⃣ Assert status code
        assertEquals("Status code mismatch", expectedStatusCode.intValue(), loginResponse.getStatusCode());

        // 2️⃣ Extract token/user id (already extracted in @Given, but you can re-extract if needed)
        String extractedToken = loginResponse.jsonPath().getString("results.user.id");
        System.out.println("Extracted Token/User ID in @When step: " + extractedToken);

    }

    @When("I add a <product> to the cart and save the order.")
    public void i_add_a_product_to_the_cart_and_save_the_order() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("I place <product> final order on mother portal.")
    public void i_place_product_final_order_on_mother_portal() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("final order status code should be {int} and show the message.")
    public void final_order_status_code_should_be_and_show_the_message(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
