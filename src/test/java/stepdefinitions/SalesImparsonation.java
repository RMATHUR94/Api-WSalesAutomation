package stepdefinitions;

import Utils.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class SalesImparsonation {

//    private final TestContext context;
//    private Response customerListResponse;
//    private JsonPath customerListJsonPath;
//
//    private JsonPath jsonPathList;
//
//    private Response impersonationResponse;
//    private String impersonationToken;
//
//    public SalesImparsonation(TestContext context) {
//        this.context = context;
//    }
}
//    @When("I checking the customer list for Essex Brownell")
//    public void i_checking_the_customer_list_for_essex_brownell() {
//        String token = context.getAccessToken();
//        customerListResponse = given()
//                .header("Authorization", "Bearer " + token)
//                .queryParam("search", "")
//                .queryParam("type", "individual")
//                .queryParam("company_id_id", "65df068605226f001269bf30")
//                .header("Content-Type", "application/json")
//                .log().all()
//                .when()
//                .get("/customer-sales-user-mapping")
//                .then()
//                .statusCode(200)
//                .log().all()
//                .extract()
//                .response();
//
//        customerListJsonPath = customerListResponse.jsonPath();
//        context.setLastResponse(customerListResponse);
//    }
//
//    @Then("the customerList response status code should be {int}")
//    public void the_customer_list_response_status_code_should_be(Integer int1) {
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }
//
//    @Then("the response message should be present")
//    public void the_response_message_should_be_present() {
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }
//
//    @Then("I extract the total number of mapped customers")
//    public void i_extract_the_total_number_of_mapped_customers() {
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }
//
//    @Then("I loop through the customer records and print details")
//    public void i_loop_through_the_customer_records_and_print_details() {
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }
//
//    @Then("I store the first customer record for impersonation")
//    public void i_store_the_first_customer_record_for_impersonation() {
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }
//
//    @When("I impersonate the first customer with device and component details")
//    public void i_impersonate_the_first_customer_with_device_and_component_details() {
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }
//
//    @Then("the impersonation response status code should be {int}")
//    public void the_impersonation_response_status_code_should_be(Integer int1) {
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }
//
//    @Then("the impersonation token should be present")
//    public void the_impersonation_token_should_be_present() {
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }
//
//    @Then("the impersonation message should be printed")
//    public void the_impersonation_message_should_be_printed() {
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }
//
//}


//    @When("I checking the customer list for Essex Brownell")
//    public void iCheckingTheCustomerListForEssexBrownell(DataTable dataTable) {
//        //Map<String, String> params = dataTable.asMap(String.class, String.class);
//        String token = context.getAccessToken();
//        customerListResponse = given()
//                .header("Authorization", "Bearer " + token)
//                .queryParam("search", "")
//                .queryParam("type", "individual")
//                .queryParam("company_id_id", "65df068605226f001269bf30")
//                .header("Content-Type", "application/json")
//                .log().all()
//                .when()
//                .get("/customer-sales-user-mapping")
//                .then()
//                .statusCode(200)
//                .log().all()
//                .extract()
//                .response();
//
//        customerListJsonPath = customerListResponse.jsonPath();
//        context.setLastResponse(customerListResponse);
//    }  Commented out code

//    @When("I checking the customer list for Essex Brownell")
//    public void i_checking_the_customer_list_for_essex_brownell() {
//        //Map<String, String> params = dataTable.asMap(String.class, String.class);
//        String token = context.getAccessToken();
//        customerListResponse = given()
//                .header("Authorization", "Bearer " + token)
//                .queryParam("search", "")
//                .queryParam("type", "individual")
//                .queryParam("company_id_id", "65df068605226f001269bf30")
//                .header("Content-Type", "application/json")
//                .log().all()
//                .when()
//                .get("/customer-sales-user-mapping")
//                .then()
//                .statusCode(200)
//                .log().all()
//                .extract()
//                .response();
//
//        customerListJsonPath = customerListResponse.jsonPath();
//        context.setLastResponse(customerListResponse);
//    }
//
//    @Then("the customerList response status code should be {int}")
//    public void the_customer_list_response_status_code_should_be(Integer int1) {
//        assertEquals((int) int1, customerListResponse.getStatusCode());
//    }
//    @Then("the response message should be present")
//    public void the_response_message_should_be_present() {
//        String message = customerListJsonPath.getString("message");
//        assertNotNull("Message should not be null", message);
//        System.out.println("Response Message: " + message);
//    }
//    @Then("I extract the total number of mapped customers")
//    public void i_extract_the_total_number_of_mapped_customers() {
//        int total = customerListJsonPath.getList("data").size();
//        System.out.println("Total Customers: " + total);
//        assertTrue("No customers found", total > 0);
//    }
//    @Then("I loop through the customer records and print details")
//    public void i_loop_through_the_customer_records_and_print_details() {
//        int totalRecords = customerListJsonPath.getList("data").size();
//
//        for (int i = 0; i < totalRecords; i++) {
//            int id = customerListJsonPath.getInt("data[" + i + "].id");
//            String swellId = customerListJsonPath.getString("data[" + i + "].customer_swell_id");
//            boolean approved = customerListJsonPath.getBoolean("data[" + i + "].is_approved");
//            String email = customerListJsonPath.getString("data[" + i + "].email");
//            String name = customerListJsonPath.getString("data[" + i + "].name");
//
//            System.out.println("\nCustomer Record " + (i + 1) + ":");
//            System.out.println("ID: " + id);
//            System.out.println("Swell ID: " + swellId);
//            System.out.println("Approved: " + approved);
//            System.out.println("Email: " + email);
//            System.out.println("Name: " + name);
//        }
//    }
//    @Then("I store the first customer record for impersonation")
//    public void i_store_the_first_customer_record_for_impersonation() {
//        int id = customerListJsonPath.getInt("data[0].id");
//        String swellId = customerListJsonPath.getString("data[0].customer_swell_id");
//
//        context.setSelectedCustomerId(id);  // store in context
//        context.setSelectedCustomerSwellId(swellId);
//
//        System.out.println("Stored for Impersonation -> ID: " + id + ", Swell ID: " + swellId);
//    }
//    @When("I impersonate the first customer with device and component details")
//    public void i_impersonate_the_first_customer_with_device_and_component_details() {
//        int customerId = context.getSelectedCustomerId();
//        String token = context.getAccessToken();
//
//        String requestBody = "{\n" +
//                "    \"customerId\": " + customerId + ",\n" +
//                "    \"deviceId\": \"6f146c34cbde1d5f604e12d30c5d691cebcf17aa766166d6efb3bb532c997141\",\n" +
//                "    \"components\": {\n" +
//                "        \"userAgent\": {\n" +
//                "            \"value\": \"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Mobile Safari/537.36\"\n" +
//                "        },\n" +
//                "        \"language\": {\n" +
//                "            \"value\": \"en-GB\"\n" +
//                "        },\n" +
//                "        \"screenResolution\": {\n" +
//                "            \"value\": [953, 845]\n" +
//                "        }\n" +
//                "    }\n" +
//                "}";
//
//        impersonationResponse = given()
//                .header("Authorization", "Bearer " + token)
//                .header("Content-Type", "application/json")
//                .body(requestBody)
//                .log().all()
//                .when()
//                .post("/customer-impersonate")
//                .then()
//                .log().all()
//                .extract()
//                .response();
//
//        context.setLastResponse(impersonationResponse);
//    }
//    @Then("the impersonation response status code should be {int}")
//    public void the_impersonation_response_status_code_should_be(Integer expectedStatusCode) {
//        assertEquals((int) expectedStatusCode, impersonationResponse.getStatusCode());
//    }
//    @Then("the impersonation token should be present")
//    public void the_impersonation_token_should_be_present() {
//        JsonPath jsonPath = impersonationResponse.jsonPath();
//        String token = jsonPath.getString("data.token.token");
//        assertNotNull("Impersonation token is missing", token);
//        System.out.println("Impersonation Token: " + token);
//        context.setImpersonationToken(token); // ✅ Store in TestContext
//    }
//    @Then("the impersonation message should be printed")
//    public void the_impersonation_message_should_be_printed() {
//        String message = impersonationResponse.jsonPath().getString("message");
//        assertNotNull("Impersonation message is missing", message);
//        System.out.println("Impersonation Message: " + message);
//    }
//
//    @When("I checking the customer list for Essex Brownell")
//    public void iCheckingTheCustomerListForEssexBrownell() {
//    }
//}
