import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class SalesUser {

    static  String token;
    static String tokenImp;
    public static void main(String[] args) {
        // Set Base URI for all requests
        RestAssured.baseURI = "https://stagingapi.essexbrownell.com";


        // ------------------ Step 1: Login ------------------
        String requestBody = "{\n" +
                "    \"email\": \"rahul.mathur@codeclouds.in\",\n" +
                "    \"password\": \"R@hul1234\",\n" +
                "    \"rememberMe\": false,\n" +
                "    \"deviceId\": \"7bc2c71ac5ff49f85facc63022b7b17575048bf93cd2e6a002e6d4883c488034\",\n" +
                "    \"components\": {\n" +
                "        \"userAgent\": {\n" +
                "            \"value\": \"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1\"\n" +
                "        },\n" +
                "        \"language\": {\n" +
                "            \"value\": \"en-GB\"\n" +
                "        },\n" +
                "        \"screenResolution\": {\n" +
                "            \"value\": [\n" +
                "                390,\n" +
                "                844\n" +
                "            ]\n" +
                "        }\n" +
                "    }\n" +
                "}";

        Response SalesloginResponse = given()
                .contentType("application/json")
                .body(requestBody).log().all()
                .when()
                .post("/auth/login")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("OTP sent to your email"))
                .extract().response();


        JsonPath json = SalesloginResponse.jsonPath();

        // Extract values
        String otpCode = json.getString("data.otpCode");
        token = json.getString("data.token");
        String message = json.getString("message");

        System.out.println(" otpcode " + otpCode + "\n" + " Token " + token + "\n" + " message " + message);

        // ------------------ Step 2: OTP Verification ------------------
        // Verifying otp and saving the response of AccessToken and refreshToken
        String recBodyOtpVerify = "{\n" +
                "    \"email\": \"rahul.mathur@codeclouds.in\",\n" +
                "    \"otp\": \"" + otpCode + "\",\n" +
                "    \"deviceId\": \"7bc2c71ac5ff49f85facc63022b7b17575048bf93cd2e6a002e6d4883c488034\",\n" +
                "    \"components\": {\n" +
                "        \"userAgent\": {\n" +
                "            \"value\": \"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1\"\n" +
                "        },\n" +
                "        \"language\": {\n" +
                "            \"value\": \"en-GB\"\n" +
                "        },\n" +
                "        \"screenResolution\": {\n" +
                "            \"value\": [\n" +
                "                390,\n" +
                "                844\n" +
                "            ]\n" +
                "        }\n" +
                "    },\n" +
                "    \"rememberMe\": false\n" +
                "}";

        Response SalesOtpResponse = given()
                .contentType("application/json")
                .body(recBodyOtpVerify).log().all()
                .when()
                .post("auth/verify-otp")
                .then().log().all()
                .statusCode(200)
                .extract().response();

        JsonPath json2 = SalesOtpResponse.jsonPath();

        String accessToken = json2.getString("meta.accessToken");
        String refreshToken = json2.getString("meta.refreshToken");
        String otpVerifyMsg = json2.getString("message");

        System.out.println("OTP Verify Message: " + otpVerifyMsg);
        System.out.println("Access Token: " + accessToken);
        System.out.println("Refresh Token: " + refreshToken);

        // ------------------ Step 3: Get Customers List -----------------
        // Company listing extracting : Company List under Sales user

        Response customerListResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("page", 1)
                .queryParam("limit", 10)
                .queryParam("search", "")
                .queryParam("type", "business")
                .queryParam("sortBy", "id")
                .queryParam("sortOrder", "desc")
                .log().all()
                .when()
                .get("/customers")
                .then()
                .log().all()
                .statusCode(200)
                .body("message", equalTo("Customers fetched successfully"))
                .extract().response();

        // Extract using JsonPath
        JsonPath jsonPathlIST = customerListResponse.jsonPath();

        // Extract fields from "data[0]"
        int id = jsonPathlIST.getInt("data[0].id");
        String customerSwellId = jsonPathlIST.getString("data[0].customer_swell_id");
        String qadId = jsonPathlIST.getString("data[0].qad_id");
        String email = jsonPathlIST.getString("data[0].email");
        String type = jsonPathlIST.getString("data[0].type");
        boolean isApproved = jsonPathlIST.getBoolean("data[0].is_approved");
        String name = jsonPathlIST.getString("data[0].name");

        // Nested object - billing
        String billingCity = jsonPathlIST.getString("data[0].billing.city");
        String billingZip = jsonPathlIST.getString("data[0].billing.zip");
        String billingState = jsonPathlIST.getString("data[0].billing.state");
        String billingAddress = jsonPathlIST.getString("data[0].billing.address1");

        // Meta fields
        int totalItems = jsonPathlIST.getInt("meta.totalItems");
        String salesUserName = jsonPathlIST.getString("meta.salesUserName");

        // Print extracted values
        System.out.println("Customer ID: " + id);
        System.out.println("QAD ID: " + qadId);
        System.out.println("Email: " + email);
        System.out.println("Type: " + type);
        System.out.println("Approved: " + isApproved);
        System.out.println("Name: " + name);
        System.out.println("Billing City: " + billingCity);
        System.out.println("Billing ZIP: " + billingZip);
        System.out.println("Billing Address: " + billingAddress);
        System.out.println("Total Items: " + totalItems);
        System.out.println("Sales User: " + salesUserName);

        //----------------- Step 4: Customer-Sales-User-Mapping : Essex Brownell Test Account - Customers List ------------------
        //Essex Brownell Test Account - Customers List

        Response responseEBCustomersList = given()
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("search", "")
                .queryParam("type", "individual")
                .queryParam("company_id_id", "65df068605226f001269bf30")
                .header("Content-Type", "application/json")
                .log().all()
                .when()
                .get("/customer-sales-user-mapping")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();

        JsonPath JsonPathEb = responseEBCustomersList.jsonPath();

        // Get the message
        String messageEB = JsonPathEb.getString("message");
        System.out.println("Response Message: " + messageEB);

        // Loop through the data array
        int totalRecords = JsonPathEb.getList("data").size();
        System.out.println("Total Records: " + totalRecords);
        // Declare variables before the loop
        int selectedRahulId = 0;
        String selectedRahulSwellId = null;
        boolean selectedRahulIsApproved = false;
        String selectedRahulEmailId = null;
        String selectedRahulNameEB = null;

        // Loop through the records
        for (int i = 0; i < totalRecords; i++) {
            int Eb_id = JsonPathEb.getInt("data[" + i + "].id");
            String swellId = JsonPathEb.getString("data[" + i + "].customer_swell_id");
            boolean isApprovedd = JsonPathEb.getBoolean("data[" + i + "].is_approved");
            String emailId = JsonPathEb.getString("data[" + i + "].email");
            String nameEB = JsonPathEb.getString("data[" + i + "].name");

            System.out.println("\nRecord " + (i + 1) + ":");
            System.out.println("ID: " + Eb_id);
            System.out.println("Customer Swell ID: " + swellId);
            System.out.println("Is Approved: " + isApprovedd);
            System.out.println("emailID: " + emailId);
            System.out.println("nameEB: " + nameEB);

            // Save only the first record (or add condition here)
            if (i == 0) {
                selectedRahulId = Eb_id;
                selectedRahulSwellId = swellId;
                selectedRahulIsApproved = isApprovedd;
                selectedRahulEmailId = emailId;
                selectedRahulNameEB = nameEB;
            }
        }

// Now use selectedId, selectedSwellId, etc. outside the loop
        System.out.println("\nRahul Customer (outside loop):");
        System.out.println("ID: " + selectedRahulId);
        System.out.println("Customer Swell ID: " + selectedRahulSwellId);
        System.out.println("Is Approved: " + selectedRahulIsApproved);
        System.out.println("Email: " + selectedRahulEmailId);
        System.out.println("Name: " + selectedRahulNameEB);



            //-------- Imparsonate Sales users to - Essex Portals ------------------------------------

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
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(requestBodyImp)
                    .log().all()
                    .when()
                    .post("/customer-impersonate")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .extract()
                    .response();

            // Extract values from JSON
//            System.out.println("Imparsonate Sales users to : 2567");
//            JsonPath jsonCu = responseCustomerImp.jsonPath();
//
//            if (jsonCu.get("data.token.token") != null) {
//                tokenImp = jsonCu.getString("data.token.token");
//                // Output of imparsonate token
//                System.out.println("Impersonation Token: " + tokenImp);
//
//            } else {
//                System.out.println("Impersonation token is null or missing");
//            }
//            String messageCImp = jsonCu.getString("message");
//            // Output
//            System.out.println("Message: " + messageCImp);


        // Extract values from JSON response
         // System.out.println("Impersonate Sales user to: 2567");

        // Parse the response once
        JsonPath jsonCu = responseCustomerImp.jsonPath();

        // Safely extract the nested impersonation token
        // String tokenImp = jsonCu.getString("data.token.token");
          //   if (tokenImp != null && !tokenImp.isEmpty()) {
               // System.out.println("Impersonation Token: " + tokenImp);
            //  } else {
              //   System.out.println("Impersonation token is null or missing");
            // }

        // Extract the top-level message
      //  String messageCImp = jsonCu.getString("message");
    //    System.out.println("Message: " + messageCImp);

        // Optional: extract the inner data.message if you also need it
//        String innerMessage = jsonCu.getString("data.message");
//        System.out.println("Inner Message: " + innerMessage);

        String topMessage   = responseCustomerImp.jsonPath().getString("message");
        String innerMessage = responseCustomerImp.jsonPath().getString("data.message");
        String impToken     = responseCustomerImp.jsonPath().getString("data.token.token");
        String otpimp = responseCustomerImp.jsonPath().getString("data.otpCode");


        System.out.println("Top-level Message : " + topMessage);   // → "Device already trusted. OTP skipped."
        System.out.println("Inner Message     : " + innerMessage); // → "Device already trusted. OTP skipped."
        System.out.println("Impersonation Token: " + impToken);
        System.out.println("Extracted OTP: " + otpimp);


        // Store it wherever you need
        System.out.println("Extracted OTP: " + otpCode);
        //OTP for imparsonation not needed as because its remembered --- Otp skipped
        // Write down the Otp for imparsonation later //

 //------- imp Otp verification ------------------------------------//

        String impOtpverifyBody = "{\n" +
                "  \"email\": \"rahul.mathur@codeclouds.in\",\n" +
                "  \"otp\": \"" + otpimp + "\",\n" +
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
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
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
        System.out.println("Impersonate Token after verifying otp: " + impersonateToken);

//--------------------- Swell Cart API - Add to Cart --------------------- //
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

        // Create RequestSpecification
        RequestSpecification requestSwellcart   = RestAssured
                .given()
                .baseUri("https://dev.d35iy77kbiv1w7.amplifyapp.com")
                .basePath("/api/swell/revalidate/cart")
                .header("Content-Type", "application/json")
                .body(addToCart).log().all();

        Response response = requestSwellcart
                .when().log().all()
                .post();

        // Extract status code
        int statusCode = response.getStatusCode();
        // Parse the response JSON
        JsonPath jsonPathswel = response.jsonPath();
        boolean swellStatas = jsonPathswel.getBoolean("status"); // true or false
        String message2 = jsonPathswel.getString("msg");     // "Item added successfully."

        // Output (AFTER variables are declared)
        System.out.println("Status Code: " + statusCode);
        System.out.println("Status Flag: " + swellStatas);
        System.out.println("Message: " + message2);

        // Saved OrderRequestBodySaveOrder : Throwing internal server error 500
        // This is because the request body is not properly formatted or the endpoint is not set up
        // Define JSON body
//        String requestBodySaveOrder =  "{\n" +
//                "    \"items\": [\n" +
//                "        {\n" +
//                "            \"product_id\": \"65cf531b86565000124b1814\",\n" +
//                "            \"quantity\": 1,\n" +
//                "            \"site_id\": \"412\",\n" +
//                "            \"shipment_location\": \"Denver, CO\",\n" +
//                "            \"sku\": \"DC-Q2-3233-50\",\n" +
//                "            \"image\": \"https://cdn.swell.store/essex-brownell_test/65d249df475cc50012c2e0c8/8c72f8b039788dd1ca3d469011276303/essex-brownell-dupont-molykote_1_90.png\",\n" +
//                "            \"qad_price\": {\n" +
//                "                \"sku\": \"DC-Q2-3233-50\",\n" +
//                "                \"base_price\": 1887.91,\n" +
//                "                \"qty\": 0,\n" +
//                "                \"metal_price\": 0,\n" +
//                "                \"differential_price\": 0,\n" +
//                "                \"surcharge_price\": 0,\n" +
//                "                \"freight_price\": 0,\n" +
//                "                \"package_price\": 0,\n" +
//                "                \"pallet_price\": 0,\n" +
//                "                \"metal_adder_price\": 0,\n" +
//                "                \"success\": true,\n" +
//                "                \"error\": false,\n" +
//                "                \"site_inventory\": [\n" +
//                "                    {\n" +
//                "                        \"site\": 412,\n" +
//                "                        \"site_name\": \"Denver\",\n" +
//                "                        \"site_state\": \"CO\",\n" +
//                "                        \"qty\": 0\n" +
//                "                    }\n" +
//                "                ]\n" +
//                "            },\n" +
//                "            \"differential_price\": 0,\n" +
//                "            \"show_differential_price\": false,\n" +
//                "            \"slug\": \"q2-3233-bouncing-putty-50-lb-226-kg-box\",\n" +
//                "            \"lead_time\": \"24\",\n" +
//                "            \"back_order\": true,\n" +
//                "            \"moq\": \"1\",\n" +
//                "            \"min_quantity_increment\": \"1\",\n" +
//                "            \"category_id\": \"65cb57f2e1c8600012f6acb4\",\n" +
//                "            \"options\": [\n" +
//                "                {\n" +
//                "                    \"values\": \"412\",\n" +
//                "                    \"name\": \"warehouse\",\n" +
//                "                    \"id\": \"66a8dc152fd56f0012773fce\"\n" +
//                "                }\n" +
//                "            ],\n" +
//                "            \"sale_price\": 1887.91,\n" +
//                "            \"price\": 1887.91,\n" +
//                "            \"product_original_price\": 1887.91,\n" +
//                "            \"id\": \"688517591287160012d0958f\",\n" +
//                "            \"orig_price\": 0,\n" +
//                "            \"delivery\": \"shipment\",\n" +
//                "            \"shipment_weight\": 0,\n" +
//                "            \"price_total\": 1887.91,\n" +
//                "            \"discount_total\": 0,\n" +
//                "            \"discount_each\": 0,\n" +
//                "            \"tax_total\": 0,\n" +
//                "            \"tax_each\": 0,\n" +
//                "            \"category_index\": []\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"title\": \"mmmmm\",\n" +
//                "    \"storedImpersonateToken\": \"U2FsdGVkX193apA+8JkeBmFJRt0uBYaJOsjkdSdFTkzZthRtwMRbso1fT2PiASHR\"\n" +
//                "}";
//
//
//        // Prepare request
//        RequestSpecification requestQucikOrder = RestAssured
//                .given()
//                .baseUri("https://dev.d35iy77kbiv1w7.amplifyapp.com")
//                .basePath("/api/swell/revalidate/quick-order")
//                .queryParam("secret", "ABDC")
//                .queryParam("account_id", "6874d2ea057c0400125023ee")
//                .header("Content-Type", "application/json")
//                .body(requestBodySaveOrder).log().all();
//
//        Response responseSaveOrder = requestQucikOrder
//                .when().log().all()
//                .post();
//
//        JsonPath jsonPathSvOdr = responseSaveOrder.jsonPath();
//
//        // Extract values
//        int status = jsonPathSvOdr.getInt("status");
//        String msg = jsonPathSvOdr.getString("msg");
//        String title = jsonPathSvOdr.getString("results.title");
//        String idd = jsonPathSvOdr.getString("results.idd");
//
//        // Output
//        System.out.println("Status: " + status);
//        System.out.println("Message: " + msg);
//        System.out.println("Title: " + title);
//        System.out.println("ID: " + idd);

        //---------------- Final Order - Essex Brownell Company -----------------//
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
                "  \"storedImpersonateToken\": \"{{tokenValue}}\"\n" +
                "}";


        RequestSpecification reqFinalOdr = RestAssured
                .given()
                .baseUri("https://dev.d35iy77kbiv1w7.amplifyapp.com")
                .basePath("/api/swell/revalidate/order")
                .queryParam("secret", "ABDC")
                .queryParam("account_id", "6874d2ea057c0400125023ee")
                .header("Content-Type", "application/json")
                .log().all()
                .body(BodyFinalOrder);

        Response respFinal = reqFinalOdr.post();
        respFinal.then().log().all();

        // Extract values using JsonPath
        boolean statusFinal = respFinal.jsonPath().getBoolean("status");
        String msgg = respFinal.jsonPath().getString("msg");
        String idfinal = respFinal.jsonPath().getString("results.id");

        // Output extracted values
        System.out.println("Status: " + statusFinal);
        System.out.println("Message: " + msgg);
        System.out.println("ID: " + idfinal);
        }
}
