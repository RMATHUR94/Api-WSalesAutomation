package stepdefinitions;

import Utils.ExtentManager;
import Utils.RequestSpec;
import Utils.TestContext;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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

        ExtentTest test = ExtentManager.createTest("Request Company List with Parameters");
        // Convert DataTable into a Map
        Map<String, String> params = dataTable.asMap(String.class, String.class);

        String token = context.getAccessToken();

        customerListResponse = RequestSpec.baseRequest(token)
                .queryParam("page", params.get("page"))
                .queryParam("limit", params.get("limit"))
                .queryParam("search", params.get("search"))
                .queryParam("type", params.get("type"))
                .queryParam("sortBy", params.get("sortBy"))
                .queryParam("sortOrder", params.get("sortOrder"))
                .when()
                .get("/customers")
                .then()
                .log().all()
                .extract()
                .response();

        jsonPathList = customerListResponse.jsonPath();
        context.setLastResponse(customerListResponse);

        test.pass("Company list request executed successfully");
    }

    @Then("^the response status code should be (\\d+)$")
    public void the_response_status_code_should_be(int expectedStatus) {
        ExtentTest test = ExtentManager.createTest("Request Company List with Parameters");
        int actualStatus = customerListResponse.getStatusCode();

        if (actualStatus == expectedStatus) {
            test.pass("Expected and actual status code match: " + actualStatus);
        } else {
            test.fail("Expected: " + expectedStatus + " but got: " + actualStatus);
        }

        customerListResponse.then().statusCode(expectedStatus);

    }

    @And("^the response message should be \"([^\"]*)\"$")
    public void the_response_message_should_be(String expectedMessage) {
        ExtentTest test = ExtentManager.createTest("Validate Response Message");

        String actualMessage = customerListResponse.jsonPath().getString("message");

        test.log(Status.INFO, "Actual message: " + actualMessage);

        if (actualMessage.equals(expectedMessage)) {
            test.pass("Response message matches: " + expectedMessage);
        } else {
            test.fail("Expected message: " + expectedMessage + " but got: " + actualMessage);
        }

        customerListResponse.then().body("message", equalTo(expectedMessage));
    }

    @And("^I extract the details of the first customer$")
    public void i_extract_the_details_of_the_first_customer() {
        ExtentTest test = ExtentManager.createTest("Extract First Customer Details");

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

        test.pass("Customer details extracted successfully");
    }

    @And("^I extract the meta information from the response$")
    public void i_extract_the_meta_information_from_the_response() {

        ExtentTest test = ExtentManager.createTest("Extract Meta information Details");


        int totalItems = jsonPathList.getInt("meta.totalItems");
        String salesUserName = jsonPathList.getString("meta.salesUserName");
        System.out.println("Total Items: " + totalItems);
        System.out.println("Sales User: " + salesUserName);

        test.log(Status.INFO, "Total Items: " + totalItems);
        test.log(Status.INFO, "Sales User Name: " + salesUserName);

        test.pass("Meta information extracted successfully");
    }

@When("I checking the customer list for Essex Brownell")
public void i_checking_the_customer_list_for_essex_brownell(DataTable dataTable) {
            ExtentTest test = ExtentManager.createTest("checking the customer list for Essex Brownell");

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
        test.pass("customer list for Essex Brownell extracted successfully");

    // -------- Dynamic Pass/Fail based on customer data --------
    List<?> customers = customerListJsonPath.getList("data");
    if (customers == null || customers.isEmpty())
    {
        test.fail("No customer records found for Essex Brownell");
        throw new AssertionError("Customer list is empty");
    }
    else
    {
        test.pass("Customer list extracted successfully. Total customers: " + customers.size());
    }

}

    @Then("the customerList response status code should be {int}")
    public void the_customer_list_response_status_code_should_be(Integer expectedStatusCode) {

        ExtentTest test = ExtentManager.createTest("customerList response status code validation");

        try {
            int actualStatus = customerListResponse.getStatusCode();
            if (actualStatus == expectedStatusCode)
            {
                test.pass("Status code validation passed: " + actualStatus);
            }
            else
            {
                test.fail("Status code validation failed. Expected: " + expectedStatusCode + ", Actual: " + actualStatus);
            }
            assertEquals((int) expectedStatusCode, actualStatus);
        }
        catch (Exception e)
        {
            test.fail("Exception during status code validation: " + e.getMessage());
            throw e;
        }
    }

    @Then("the response message should be present")
    public void the_response_message_should_be_present() {

        ExtentTest test = ExtentManager.createTest("Validate Response Message Presence");

        try {
            String message = customerListJsonPath.getString("message");

            if (message != null && !message.isEmpty())
            {
                test.pass("Response message is present: " + message);
            }
            else
            {
                test.fail("Response message is missing or empty");
            }
            assertNotNull("Message should not be null", message);
            System.out.println("Response Message: " + message);
        }
        catch (Exception e)
        {
            test.fail("Exception while checking response message: " + e.getMessage());
            throw e;
        }
    }

    @Then("I extract the total number of mapped customers")
    public void i_extract_the_total_number_of_mapped_customers() {
        ExtentTest test = ExtentManager.createTest("Extract Total Number of Mapped Customers");
        try {
            int total = customerListJsonPath.getList("data").size();
            if (total > 0)
            {
                test.pass("Total mapped customers: " + total);
            }
            else
            {
                test.fail("No customers found");
            }
            assertTrue("No customers found", total > 0);

            System.out.println("Total Customers: " + total);

        }
        catch (Exception e)
        {
            test.fail("Exception while extracting total customers: " + e.getMessage());
            throw e;
        }
    }

    @Then("I loop through the customer records and print details")
    public void i_loop_through_the_customer_records_and_print_details() {

        ExtentTest test = ExtentManager.createTest("Loop Through Customer Records");

        // Loop through the Records & Print Details Of each Records
         try {
             int totalRecords = customerListJsonPath.getList("data").size();
             for (int i = 0; i < totalRecords; i++) {
                 int id = customerListJsonPath.getInt("data[" + i + "].id");
                 String swellId = customerListJsonPath.getString("data[" + i + "].customer_swell_id");
                 boolean approved = customerListJsonPath.getBoolean("data[" + i + "].is_approved");
                 String email = customerListJsonPath.getString("data[" + i + "].email");
                 String name = customerListJsonPath.getString("data[" + i + "].name");
                 test.pass("Customer #" + (i + 1) + " - ID: " + id + ", Name: " + name + ", Email: " + email + ", Approved: " + approved);

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
                     test.info("First customer stored for impersonation");
                 }
             }
             test.pass("All customer records processed successfully");
         }

         catch (Exception e) {
             test.fail("Exception while looping through customer records: " + e.getMessage());
             throw e;
         }
    }

        @Then("I store the first customer record for impersonation")
    public void i_store_the_first_customer_record_for_impersonation() {
            ExtentTest test = ExtentManager.createTest("Store First Customer for Impersonation");

            try {
                int id = context.getSelectedCustomerId();
                String swellId = context.getSelectedCustomerSwellId();
                String email = context.getselectedCustomerEmail();
                test.pass("Stored first customer -> ID: " + id + ", SwellId: " + swellId + ", Email: " + email);

                System.out.println("Using stored values -> ID: " + id + ", SwellId: " + swellId + ", Email: " + email);
                System.out.println("Using stored values -> ID: " + id +
                        ", SwellId: " + swellId +
                        ", Email: " + email);

            }
            catch (Exception e) {
                test.fail("Exception while storing first customer: " + e.getMessage());
                throw e;
            }



    }

        @When("I impersonate the first customer with device and component details")
    public void i_impersonate_the_first_customer_with_device_and_component_details() {

        ExtentTest test = ExtentManager.createTest("Impersonate First Customer");

        try {
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

            impersonationResponse = RequestSpec.baseRequest(token)
                    .body(requestBody)
                    .when()
                    .post("/customer-impersonate")
                    .then()
                    .log().all()
                    .extract()
                    .response();

            context.setLastResponse(impersonationResponse);
            String message = impersonationResponse.jsonPath().getString("message");
            if (message != null) test.pass("Impersonation request successful: " + message);
            else test.fail("Impersonation request failed: message missing");

        }
        catch (Exception e) {
            test.fail("Exception during impersonation: " + e.getMessage());
            throw e;
        }

    }

    @Then("the impersonation response status code should be {int}")
    public void the_impersonation_response_status_code_should_be(Integer expectedStatusCode) {
        ExtentTest test = ExtentManager.createTest("Validate Impersonation Response Status Code");
        try {
            int actualStatus = impersonationResponse.getStatusCode();
            if (actualStatus == expectedStatusCode) {
                test.pass("Impersonation status code matched: " + actualStatus);
            } else {
                test.fail("Expected status: " + expectedStatusCode + ", but got: " + actualStatus);
            }
            assertEquals((int) expectedStatusCode, actualStatus);
        } catch (Exception e) {
            test.fail("Exception in status code validation: " + e.getMessage());
            throw e;
        }
    }

    @And("the impersonation token should be present")
    public void theImpersonationTokenShouldBePresent() {
        ExtentTest test = ExtentManager.createTest("Check Impersonation Token Presence");
        try {
            String token = impersonationResponse.jsonPath().getString("data.token.token");
            if (token != null && !token.isEmpty()) {
                test.pass("Impersonation token extracted: " + token);
                context.setImpersonationToken(token);
            } else {
                test.fail("Impersonation token is missing or empty");
                throw new AssertionError("Impersonation token missing");
            }
        } catch (Exception e) {
            test.fail("Exception while extracting impersonation token: " + e.getMessage());
            throw e;
        }
    }

    @Then("the impersonation message should be printed")
    public void the_impersonation_message_should_be_printed() {
        ExtentTest test = ExtentManager.createTest("Print Impersonation Message");
        try {
            String message = impersonationResponse.jsonPath().getString("message");
            if (message != null && !message.isEmpty()) {
                test.pass("Impersonation message: " + message);
                context.setImpersonationMessage(message);
            } else {
                test.fail("Impersonation message is missing");
                throw new AssertionError("Impersonation message missing");
            }
        } catch (Exception e) {
            test.fail("Exception while getting impersonation message: " + e.getMessage());
            throw e;
        }
    }

    @And("I verify the impersonation details")
    public void iVerifyTheImpersonationDetails() {
        ExtentTest test = ExtentManager.createTest("Verify Impersonation Details");
        try {
            String token = context.getImpersonationToken();
            String message = context.getImpersonationMessage();
            test.pass("Stored Token: " + token);
            test.pass("Stored Message: " + message);
            System.out.println("Stored Token: " + token);
            System.out.println("Stored Message: " + message);
        } catch (Exception e) {
            test.fail("Exception while verifying impersonation details: " + e.getMessage());
            throw e;
        }
    }

    @When("I impersonate the customer under Essex Brownell company")
    public void iImpersonateTheCustomerUnderEssexBrownellCompany() {
        ExtentTest test = ExtentManager.createTest("Impersonate Customer under Essex Brownell");
        try {
            String token = context.getAccessToken();
            String requestBodyImp = "{\n" + " \"customerId\": 1450,\n" + " \"deviceId\": \"6f146c34cbde1d5f604e12d30c5d691cebcf17aa766166d6efb3bb532c997141\",\n" + " \"components\": {\n" + " \"userAgent\": {\n" + " \"value\": \"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Mobile Safari/537.36\"\n" + " },\n" + " \"language\": {\n" + " \"value\": \"en-GB\"\n" + " },\n" + " \"screenResolution\": {\n" + " \"value\": [953, 845]\n" + " }\n" + " }\n" + "}"; // keep original JSON
            Response responseCustomerImp = RequestSpec.baseRequest(token)
                    .body(requestBodyImp)
                    .when()
                    .post("/customer-impersonate")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .extract()
                    .response();

            String topMessage = responseCustomerImp.jsonPath().getString("message");
            String otpimp = responseCustomerImp.jsonPath().getString("data.otpCode");
            context.setImpersonationMessage(topMessage);
            context.SetImpersonationOtp(otpimp);

            test.pass("Impersonation request message: " + topMessage);
            test.pass("OTP extracted: " + otpimp);
            System.out.println("Top-level Message : " + topMessage);
            System.out.println("Extracted OTP     : " + otpimp);
        } catch (Exception e) {
            test.fail("Failed to impersonate customer: " + e.getMessage());
            throw e;
        }

    }

    @Then("I have received imparsonation otp and message")
    public void iHaveReceivedImparsonationOtpAndMessage() {
        ExtentTest test = ExtentManager.createTest("Verify OTP and Impersonation Message");
        try {
            String message = context.getImpersonationMessage();
            String otp = context.GetImpersonationOtp();
            if ("OTP sent successfully".equals(message)) {
                test.pass("Expected message received: " + message);
            } else {
                test.fail("Expected message 'OTP sent successfully', but got: " + message);
            }

            if (otp != null && !otp.trim().isEmpty()) {
                test.pass("OTP is present: " + otp);
            } else {
                test.fail("OTP is missing or empty");
                throw new AssertionError("OTP missing");
            }

            System.out.println("Top-level Message : " + message);
            System.out.println("Extracted OTP     : " + otp);
        } catch (Exception e) {
            test.fail("Exception while verifying OTP and message: " + e.getMessage());
            throw e;
        }
    }

    @When("I verify the impersonation otp for the customer")
    public void iVerifyTheImpersonationOtpForTheCustomer() {
        ExtentTest test = ExtentManager.createTest("Verify Impersonation OTP for Customer");
        try {
            String token = context.getAccessToken();
            String otpImp = context.GetImpersonationOtp();
            String impOtpverifyBody = "{\n" + " \"email\": \"rahul.mathur@codeclouds.in\",\n" + " \"otp\": \"" + otpImp + "\",\n" + " \"customerId\": 1450,\n" + " \"rememberMe\": false,\n" + " \"deviceId\": \"bcc6990218bdb1cc23e779cd21280105fb384304197dd87c1fe8ba8826a8b2ff\",\n" + " \"components\": {\n" + " \"browser\": \"Chrome\",\n" + " \"os\": \"Windows\"\n" + " }\n" + "}"; // keep original JSON with otpImp

            Response impOtpVerifyRes = RestAssured.given()
                    .spec(RequestSpec.baseRequest(token))
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
            test.pass("Impersonation token after OTP verification: " + impersonateToken);
            System.out.println("Impersonate Token after verifying otp: " + impersonateToken);
        } catch (Exception e) {
            test.fail("Failed to verify OTP: " + e.getMessage());
            throw e;
        }
    }


    @Then("I should get a imparsonation access token and message")
    public void iShouldGetAImparsonationAccessTokenAndMessage() {
        ExtentTest test = ExtentManager.createTest("Check Final Impersonation Access Token");
        try {
            String token = context.getImpersonationToken();
            if (token != null && !token.isEmpty()) {
                test.pass("Access token retrieved: " + token);
            } else {
                test.fail("Access token is missing");
                throw new AssertionError("Impersonation access token missing");
            }
            System.out.println("Impersonate Token after verifying otp:" + token);
        } catch (Exception e) {
            test.fail("Exception in access token validation: " + e.getMessage());
            throw e;
        }
    }


    @Given("When I add a product to the cart on storefront as an impersonated user")
    public void whenIAddAProductToTheCartOnStorefrontAsAnImpersonatedUser() throws IOException {
        ExtentTest test = ExtentManager.createTest("Add Product to Cart as Impersonated User");
        try {
            String addToCartJson = new String(Files.readAllBytes(Paths.get("src/test/java/resources/payloads/addToCart.json")));
            requestSwellcart = RequestSpec.swellRequest()
                    .basePath("/api/swell/revalidate/cart")
                    .body(addToCartJson)
                    .log().all();
            test.pass("Add-to-cart request prepared with JSON payload");
        } catch (Exception e) {
            test.fail("Failed to prepare add-to-cart request: " + e.getMessage());
            throw e;
        }
    }

    @Then("The status code should be {int} and show the message")
    public void theStatusCodeShouldBeAndShowTheMessage(int arg0) {
        ExtentTest test = ExtentManager.createTest("Add-to-Cart Status and Message Verification");
        try {
            Response response = requestSwellcart.when().log().all().post();
            int actualStatus = response.getStatusCode();
            JsonPath jp = response.jsonPath();
            boolean status = jp.getBoolean("status");
            String message = jp.getString("msg");

            if (actualStatus == arg0) test.pass("Add-to-cart status code: " + actualStatus);
            else test.fail("Expected status " + arg0 + ", got " + actualStatus);

            test.pass("Add-to-cart message: " + message);
            System.out.println("Status Code: " + actualStatus);
            System.out.println("Status Flag: " + status);
            System.out.println("Message: " + message);
        } catch (Exception e) {
            test.fail("Exception while verifying add-to-cart response: " + e.getMessage());
            throw e;
        }
    }

    @When("final order placement on storefront as an impersonated user")
    public void finalOrderPlacementOnStorefrontAsAnImpersonatedUser() throws IOException {
       String impToken = context.getImpersonationToken();
        // Read JSON payload from file
        String payload = new String(Files.readAllBytes(Paths.get("src/test/java/resources/payloads/finalOrder.json")));
        // Replace placeholder with actual token
        payload = payload.replace("impToken", context.getImpersonationToken());;


        responseFinalOrder =  RequestSpec.swellRequest()
                .basePath("/api/swell/revalidate/order")
                .queryParam("secret", "ABDC")
                .queryParam("account_id", "6874d2ea057c0400125023ee")
                .header("Content-Type", "application/json")
                .body(payload)
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
