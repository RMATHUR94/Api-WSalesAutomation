package stepdefinitions;

import Utils.RequestSpec;
import Utils.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.testng.asserts.SoftAssert;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class CompanyList {


    private final TestContext context;
    private Response customerListResponse;
    private JsonPath customerListJsonPath;
    private JsonPath jsonPathList;
    private Response impersonationResponse;


    private RequestSpecification requestSwellcart;
    private Response responseFinalOrder;

    public CompanyList(TestContext context) {
        this.context = context;
    }


    @When("^I request the company list with parameters$")
    public void i_request_the_company_list_with_parameters(DataTable dataTable) {
        // Convert DataTable into a Map
        Map<String, String> params = dataTable.asMap(String.class, String.class);

        String token = context.getAccessToken();
        customerListResponse = given()
                .spec(RequestSpec.get())
                .header("Authorization", "Bearer " + token)
                .queryParam("page", params.get("page"))
                .queryParam("limit", params.get("limit"))
                .queryParam("search", params.get("search"))
                .queryParam("type", params.get("type"))
                .queryParam("sortBy", params.get("sortBy"))
                .queryParam("sortOrder", params.get("sortOrder"))
                .log().all()
                .when()
                .get("/customers")
                .then()
                .log().all()
                .extract()
                .response();

        jsonPathList = customerListResponse.jsonPath();
        context.setLastResponse(customerListResponse);
    }

    @Then("^the response status code should be (\\d+)$")
    public void the_response_status_code_should_be(int expectedStatus) {
        customerListResponse.then().statusCode(expectedStatus);
    }

    @And("^the response message should be \"([^\"]*)\"$")
    public void the_response_message_should_be(String expectedMessage) {
        customerListResponse.then().body("message", equalTo(expectedMessage));
    }

    @And("^I extract the details of the first customer$")
    public void i_extract_the_details_of_the_first_customer() {
        int id = jsonPathList.getInt("data[0].id");
        String customerSwellId = jsonPathList.getString("data[0].customer_swell_id");
        String qadId = jsonPathList.getString("data[0].qad_id");
        String email = jsonPathList.getString("data[0].email");
        String type = jsonPathList.getString("data[0].type");
        boolean isApproved = jsonPathList.getBoolean("data[0].is_approved");
        String name = jsonPathList.getString("data[0].name");

        String billingCity = jsonPathList.getString("data[0].billing.city");
        String billingZip = jsonPathList.getString("data[0].billing.zip");
        String billingState = jsonPathList.getString("data[0].billing.state");
        String billingAddress = jsonPathList.getString("data[0].billing.address1");

        System.out.println("Customer ID: " + id);
        System.out.println("QAD ID: " + qadId);
        System.out.println("Email: " + email);
        System.out.println("Type: " + type);
        System.out.println("Approved: " + isApproved);
        System.out.println("Name: " + name);
        System.out.println("Billing City: " + billingCity);
        System.out.println("Billing ZIP: " + billingZip);
        System.out.println("Billing State: " + billingState);
        System.out.println("Billing Address: " + billingAddress);
    }

    @And("^I extract the meta information from the response$")
    public void i_extract_the_meta_information_from_the_response() {
        int totalItems = jsonPathList.getInt("meta.totalItems");
        String salesUserName = jsonPathList.getString("meta.salesUserName");

        System.out.println("Total Items: " + totalItems);
        System.out.println("Sales User: " + salesUserName);
    }

@When("I checking the customer list for Essex Brownell")
public void i_checking_the_customer_list_for_essex_brownell(DataTable dataTable) {

            String token = context.getAccessToken();
            Map<String, String> params = dataTable.asMap(String.class, String.class);
        customerListResponse = given()
                .spec(RequestSpec.get())
                .header("Authorization", "Bearer " + token)
                .queryParams(params)
                .header("Content-Type", "application/json")
                .log().all()
                .when()
                .get("/customer-sales-user-mapping")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();

        customerListJsonPath = customerListResponse.jsonPath();
        context.setLastResponse(customerListResponse);
}

    @Then("the customerList response status code should be {int}")
    public void the_customer_list_response_status_code_should_be(Integer expectedStatusCode) {
        assertEquals((int) expectedStatusCode, customerListResponse.getStatusCode());
    }

    @Then("the response message should be present")
    public void the_response_message_should_be_present() {
        String message = customerListJsonPath.getString("message");
       assertNotNull("Message should not be null", message);
        System.out.println("Response Message: " + message);
    }

    @Then("I extract the total number of mapped customers")
    public void i_extract_the_total_number_of_mapped_customers() {
        int total = customerListJsonPath.getList("data").size();
        System.out.println("Total Customers: " + total);
        assertTrue("No customers found", total > 0);
    }

    @Then("I loop through the customer records and print details")
    public void i_loop_through_the_customer_records_and_print_details() {
        int totalRecords = customerListJsonPath.getList("data").size();

        // Loop through the Records & Print Details Of each Records
        for (int i = 0; i < totalRecords; i++) {
            int id = customerListJsonPath.getInt("data[" + i + "].id");
            String swellId = customerListJsonPath.getString("data[" + i + "].customer_swell_id");
            boolean approved = customerListJsonPath.getBoolean("data[" + i + "].is_approved");
            String email = customerListJsonPath.getString("data[" + i + "].email");
            String name = customerListJsonPath.getString("data[" + i + "].name");

            System.out.println("\nCustomer Record " + (i + 1) + ":");
            System.out.println("ID: " + id);
            System.out.println("Swell ID: " + swellId);
            System.out.println("Approved: " + approved);
            System.out.println("Email: " + email);
            System.out.println("Name: " + name);

            if (i == 0) { // store Rahul record in Shared context (change edit loop int for other users)
                context.setSelectedCustomerId(id);
                context.setSelectedCustomerSwellId(swellId);
                context.setselectedCustomerEmail(email);
            }
        }
    }

        @Then("I store the first customer record for impersonation")
    public void i_store_the_first_customer_record_for_impersonation() {
            int id = context.getSelectedCustomerId();
            String swellId = context.getSelectedCustomerSwellId();
            String email = context.getselectedCustomerEmail();

            System.out.println("Using stored values -> ID: " + id +
                    ", SwellId: " + swellId +
                    ", Email: " + email);
    }

        @When("I impersonate the first customer with device and component details")
    public void i_impersonate_the_first_customer_with_device_and_component_details() {
        int customerId = context.getSelectedCustomerId();
        String swellId = context.getSelectedCustomerSwellId();
        String email = context.getselectedCustomerEmail();

        String token = context.getAccessToken();
        System.out.println(customerId + " " + swellId + " " + email);

        String requestBody = "{\n" +
                "    \"customerId\": " + 2567 + ",\n" +
                "    \"deviceId\": \"6f146c34cbde1d5f604e12d30c5d691cebcf17aa766166d6efb3bb532c997141\",\n" +
                "    \"components\": {\n" +
                "        \"userAgent\": {\n" +
                "            \"value\": \"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Mobile Safari/537.36\"\n" +
                "        },\n" +
                "        \"language\": {\n" +
                "            \"value\": \"en-GB\"\n" +
                "        },\n" +
                "        \"screenResolution\": {\n" +
                "            \"value\": [953, 845]\n" +
                "        }\n" +
                "    }\n" +
                "}";

        impersonationResponse = given()
                .spec(RequestSpec.get())
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .log().all()
                .when()
                .post("/customer-impersonate")
                .then()
                .log().all()
                .extract()
                .response();

        context.setLastResponse(impersonationResponse);
    }

    @Then("the impersonation response status code should be {int}")
    public void the_impersonation_response_status_code_should_be(Integer expectedStatusCode) {
        assertEquals((int) expectedStatusCode, impersonationResponse.getStatusCode());
    }

    @And("the impersonation token should be present")
    public void theImpersonationTokenShouldBePresent() {
        JsonPath jsonPath = impersonationResponse.jsonPath();
        String token = jsonPath.getString("data.token.token");
        assertNotNull("Impersonation token is missing", token);
        context.setImpersonationToken(token);
    }

    @Then("the impersonation message should be printed")
    public void the_impersonation_message_should_be_printed() {
        String message = impersonationResponse.jsonPath().getString("message");
        assertNotNull("Impersonation message is missing", message);
        context.setImpersonationMessage(message); ;
    }

    @And("I verify the impersonation details")
    public void iVerifyTheImpersonationDetails() {
        System.out.println("Stored Token: " + context.getImpersonationToken());
        System.out.println("Stored Message: " + context.getImpersonationMessage());
    }

    @When("I impersonate the customer under Essex Brownell company")
    public void iImpersonateTheCustomerUnderEssexBrownellCompany() {
        String token = context.getAccessToken();
        String requestBodyImp  = "{\n" +
                "  \"customerId\": 1450,\n" +
                "  \"deviceId\": \"6f146c34cbde1d5f604e12d30c5d691cebcf17aa766166d6efb3bb532c997141\",\n" +
                "  \"components\": {\n" +
                "    \"userAgent\": {\n" +
                "      \"value\": \"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Mobile Safari/537.36\"\n" +
                "    },\n" +
                "    \"language\": {\n" +
                "      \"value\": \"en-GB\"\n" +
                "    },\n" +
                "    \"screenResolution\": {\n" +
                "      \"value\": [953, 845]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        // POST request
        Response responseCustomerImp = given()
                .spec(RequestSpec.get())
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBodyImp)
                .log().all()
                .when()
                .post("/customer-impersonate")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        String topMessage   = responseCustomerImp.jsonPath().getString("message");
        String otpimp = responseCustomerImp.jsonPath().getString("data.otpCode");
        context.setImpersonationMessage(topMessage);
        context.SetImpersonationOtp(otpimp);

        System.out.println("Top-level Message : " + context.getImpersonationMessage());   // → "Device already trusted. OTP skipped."// → "Device already trusted. OTP skipped."
        System.out.println("Extracted OTP     : " + context.GetImpersonationOtp());

    }

    @Then("I have received imparsonation otp and message")
    public void iHaveReceivedImparsonationOtpAndMessage() {
        String message = context.getImpersonationMessage();
        String otp     = context.GetImpersonationOtp();
        System.out.println("Top-level Message : " + message);
        System.out.println("Extracted OTP     : " + otp);
        assertEquals("Expected message to be 'OTP sent successfully'","OTP sent successfully",context.getImpersonationMessage());
        // ✅ (Optional) Ensure OTP is not null or empty
        if (otp == null || otp.trim().isEmpty()) {
            throw new AssertionError("Imparsonation OTP is missing or empty");
        }
    }

    @When("I verify the impersonation otp for the customer")
    public void iVerifyTheImpersonationOtpForTheCustomer() {
        String token = context.getAccessToken();
        String otpImp = context.GetImpersonationOtp();

        String impOtpverifyBody = "{\n" +
                "  \"email\": \"rahul.mathur@codeclouds.in\",\n" +
                "  \"otp\": \"" + otpImp + "\",\n" +
                "  \"customerId\": 1450,\n" +
                "  \"rememberMe\": false,\n" +
                "  \"deviceId\": \"bcc6990218bdb1cc23e779cd21280105fb384304197dd87c1fe8ba8826a8b2ff\",\n" +
                "  \"components\": {\n" +
                "    \"browser\": \"Chrome\",\n" +
                "    \"os\": \"Windows\"\n" +
                "  }\n" +
                "}";
        Response impOtpVerifyRes = RestAssured
                .given()
                .spec(RequestSpec.get())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(impOtpverifyBody)
                .log().all()
                .when()
                .post("/customer-impersonate/impersonate/verify-otp")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        String impersonateToken = impOtpVerifyRes.jsonPath().getString("data.token");
        context.setImpersonationToken(impersonateToken);
        System.out.println("Impersonate Token after verifying otp: " + context.getImpersonationToken());
    }


    @Then("I should get a imparsonation access token and message")
    public void iShouldGetAImparsonationAccessTokenAndMessage() {
        System.out.println("Impersonate Token after verifying otp:" + context.getImpersonationToken());
    }


    @Given("When I add a product to the cart on storefront as an impersonated user")
    public void whenIAddAProductToTheCartOnStorefrontAsAnImpersonatedUser() {
        //Product add To cart
        String addToCart = "{\n" +
                "    \"account_id\": \"6874d2ea057c0400125023ee\",\n" +
                "    \"items\": {\n" +
                "        \"product_id\": \"65cf531b86565000124b1814\",\n" +
                "        \"quantity\": 1,\n" +
                "        \"shipment_location\": \"\",\n" +
                "        \"sku\": \"DC-Q2-3233-50\",\n" +
                "        \"image\": \"https://cdn.swell.store/essex-brownell_test/65d249df475cc50012c2e0c8/8c72f8b039788dd1ca3d469011276303/essex-brownell-dupont-molykote_1_90.png\",\n" +
                "        \"back_order\": true,\n" +
                "        \"lead_time\": \"24\",\n" +
                "        \"slug\": \"q2-3233-bouncing-putty-50-lb-226-kg-box\",\n" +
                "        \"qad_price\": {\n" +
                "            \"sku\": \"DC-Q2-3233-50\",\n" +
                "            \"base_price\": 1887.91,\n" +
                "            \"qty\": 0,\n" +
                "            \"metal_price\": 0,\n" +
                "            \"differential_price\": 0,\n" +
                "            \"surcharge_price\": 0,\n" +
                "            \"freight_price\": 0,\n" +
                "            \"package_price\": 0,\n" +
                "            \"pallet_price\": 0,\n" +
                "            \"metal_adder_price\": 0,\n" +
                "            \"success\": true,\n" +
                "            \"error\": false,\n" +
                "            \"site_inventory\": [\n" +
                "                {\n" +
                "                    \"site\": 412,\n" +
                "                    \"site_name\": \"Denver\",\n" +
                "                    \"site_state\": \"CO\",\n" +
                "                    \"qty\": 0\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        \"price\": 1887.91,\n" +
                "        \"moq\": \"1\",\n" +
                "        \"min_quantity_increment\": \"1\",\n" +
                "        \"show_differential_price\": false,\n" +
                "        \"category_id\": \"65cb57f2e1c8600012f6acb4\"\n" +
                "    },\n" +
                "    \"cart_id\": \"68825f21b158c30012f92761\"\n" +
                "}";

                requestSwellcart = RestAssured
                .given()
                .baseUri("https://dev.d35iy77kbiv1w7.amplifyapp.com")
                .basePath("/api/swell/revalidate/cart")
                .header("Content-Type", "application/json")
                .body(addToCart)
                .log().all();


    }

    @Then("The status code should be {int} and show the message")
    public void theStatusCodeShouldBeAndShowTheMessage(int arg0) {
        Response response = requestSwellcart
                .when().log().all()
                .post();

        int statusCode = response.getStatusCode();
        JsonPath jsonPathswel = response.jsonPath();
        boolean swellStatus = jsonPathswel.getBoolean("status");
        String message2 = jsonPathswel.getString("msg");

        System.out.println("Status Code: " + statusCode);
        System.out.println("Status Flag: " + swellStatus);
        System.out.println("Message: " + message2);
    }

    @When("final order placement on storefront as an impersonated user")
    public void finalOrderPlacementOnStorefrontAsAnImpersonatedUser() {
       String impToken = context.getImpersonationToken();
        String BodyFinalOrder = "{\n" +
                "  \"shipping\": {\n" +
                "    \"zip\": \"53216-1708\",\n" +
                "    \"state\": \"WI\",\n" +
                "    \"address1\": \"4170 N 35TH STREET Suote A, \\n, \\n\",\n" +
                "    \"city\": \"MILWAUKEE\",\n" +
                "    \"country\": \"US\",\n" +
                "    \"first_name\": \"Essex Brownell Company\",\n" +
                "    \"last_name\": null,\n" +
                "    \"name\": \"Essex Brownell Company\",\n" +
                "    \"id\": \"bill_6874d2ea057c0400125023ee\"\n" +
                "  },\n" +
                "  \"billing\": {\n" +
                "    \"zip\": \"53216-1708\",\n" +
                "    \"state\": \"WI\",\n" +
                "    \"address1\": \"4170 N 35TH STREET Suote A, \\n, \\n\",\n" +
                "    \"city\": \"MILWAUKEE\",\n" +
                "    \"country\": \"US\",\n" +
                "    \"first_name\": \"Essex Brownell Company\",\n" +
                "    \"last_name\": null,\n" +
                "    \"name\": \"Essex Brownell Company\",\n" +
                "    \"id\": \"bill_6874d2ea057c0400125023ee\"\n" +
                "  },\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"product_id\": \"65cf531b86565000124b1814\",\n" +
                "      \"quantity\": 1,\n" +
                "      \"site_id\": \"412\",\n" +
                "      \"shipment_location\": \"Denver, CO\",\n" +
                "      \"sku\": \"DC-Q2-3233-50\",\n" +
                "      \"image\": \"https://cdn.swell.store/essex-brownell_test/65d249df475cc50012c2e0c8/8c72f8b039788dd1ca3d469011276303/essex-brownell-dupont-molykote_1_90.png\",\n" +
                "      \"qad_price\": {\n" +
                "        \"sku\": \"DC-Q2-3233-50\",\n" +
                "        \"base_price\": 1887.91,\n" +
                "        \"qty\": 0,\n" +
                "        \"metal_price\": 0,\n" +
                "        \"differential_price\": 0,\n" +
                "        \"surcharge_price\": 0,\n" +
                "        \"freight_price\": 0,\n" +
                "        \"package_price\": 0,\n" +
                "        \"pallet_price\": 0,\n" +
                "        \"metal_adder_price\": 0,\n" +
                "        \"success\": true,\n" +
                "        \"error\": false,\n" +
                "        \"site_inventory\": [\n" +
                "          {\n" +
                "            \"site\": 412,\n" +
                "            \"site_name\": \"Denver\",\n" +
                "            \"site_state\": \"CO\",\n" +
                "            \"qty\": 0\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      \"show_differential_price\": false,\n" +
                "      \"slug\": \"q2-3233-bouncing-putty-50-lb-226-kg-box\",\n" +
                "      \"lead_time\": \"24\",\n" +
                "      \"back_order\": true,\n" +
                "      \"moq\": \"1\",\n" +
                "      \"min_quantity_increment\": \"1\",\n" +
                "      \"category_id\": \"65cb57f2e1c8600012f6acb4\",\n" +
                "      \"options\": [\n" +
                "        {\n" +
                "          \"values\": \"412\",\n" +
                "          \"name\": \"warehouse\",\n" +
                "          \"id\": \"66a8dc152fd56f0012773fce\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"sale_price\": 1887.91,\n" +
                "      \"price\": 1887.91,\n" +
                "      \"differential_price\": 0,\n" +
                "      \"product_original_price\": 1887.91,\n" +
                "      \"id\": \"68850cb65d8cdf001211ca87\",\n" +
                "      \"orig_price\": 0,\n" +
                "      \"delivery\": \"shipment\",\n" +
                "      \"shipment_weight\": 0,\n" +
                "      \"price_total\": \"1887.91\",\n" +
                "      \"discount_total\": 0,\n" +
                "      \"discount_each\": 0,\n" +
                "      \"tax_total\": 0,\n" +
                "      \"tax_each\": 0,\n" +
                "      \"category_index\": [],\n" +
                "      \"discounts\": []\n" +
                "    }\n" +
                "  ],\n" +
                "  \"purchase_order_number\": \"Final order\",\n" +
                "  \"company_id\": \"65df068605226f001269bf30\",\n" +
                "  \"storedImpersonateToken\": \"" + impToken + "\"\n" +
                "}";

                 responseFinalOrder = RestAssured
                .given().spec(RequestSpec.get())
                .baseUri("https://dev.d35iy77kbiv1w7.amplifyapp.com")
                .basePath("/api/swell/revalidate/order")
                .queryParam("secret", "ABDC")
                .queryParam("account_id", "6874d2ea057c0400125023ee")
                .header("Content-Type", "application/json")
                .body(BodyFinalOrder)
                .log().all()
                .post();
    }

    @Then("final order status code should be {int} and show the message")
    public void finalOrderStatusCodeShouldBeAndShowTheMessage(int expectedCode) {
        int actualCode = responseFinalOrder.getStatusCode();
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(actualCode, expectedCode, "Unexpected HTTP status code");

        // -------- Extract and print details --------
        JsonPath jp = responseFinalOrder.jsonPath();
        boolean statusFlag = jp.getBoolean("status");
        String msg = jp.getString("msg");
        String orderId = jp.getString("results.id");

        System.out.println("Status Code : " + actualCode);
        System.out.println("Status Flag : " + statusFlag);
        System.out.println("Message     : " + msg);
        System.out.println("Order ID    : " + orderId);

        // Optional additional validations
        soft.assertNotNull(orderId, "Order ID should not be null");

        soft.assertAll();
    }

}
