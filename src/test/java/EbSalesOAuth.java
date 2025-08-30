import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class EbSalesOAuth {
    public static void main (String[] args)
    {

        // Set Base URI for all requests
        RestAssured.baseURI = "https://stagingapi.essexbrownell.com";

        // Step 1: Login API Payload
        String requestBody = "{\n" +
                "  \"email\": \"pod20@codeclouds.biz\",\n" +
                "  \"password\": \"Essex@pod20\",\n" +
                "  \"rememberMe\": false\n" +
                "}";

        // Step 2: Perform Login and Validate Response
        Response loginResponse = given()
                .contentType("application/json")
                .body(requestBody).log().all()
                .when()
                .post("/auth/login")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Login successful"))
                .body("meta.accessToken", notNullValue())
                .extract()
                .response();

        // Step 3: Print Login Response
        System.out.println("Login Status Code: " + loginResponse.getStatusCode());
        System.out.println("Login Response Body:\n" + loginResponse.prettyPrint());

        // Step 4: Extract Token
        JsonPath jsonPath = loginResponse.jsonPath();
        String accessToken = jsonPath.getString("meta.accessToken");
        String refreshToken = jsonPath.getString("meta.refreshToken");

        System.out.println("Access Token: " + accessToken);
        System.out.println("Refresh Token: " + refreshToken);

        // Step 5: Call Sales User List API using token
        Response salesUserResponse = given()
                .header("Authorization", "Bearer " + accessToken)  // Must include "Bearer "
                .header("Content-Type", "application/json")
                .queryParam("page", 1)
                .queryParam("limit", 10)
                .queryParam("search", "")
                .queryParam("sortBy", "id")
                .queryParam("sortOrder", "desc")
                .queryParam("roleId", 2)
                .when()
                .get("/sales-users")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data", notNullValue())
                .extract()
                .response();

        // Step 6: Print Sales Users Response
        System.out.println("Sales Users API Status: " + salesUserResponse.getStatusCode());
        System.out.println("Sales Users API Response:\n" + salesUserResponse.prettyPrint());

        System.out.println("//Creating  a new Sales User");

        String salesUserPayload = "{\n" +
                "    \"salutation\": \"Mr\",\n" +
                "    \"firstName\": \"John\",\n" +
                "    \"lastName\": \"Doe\",\n" +
                "    \"roleId\": 2,\n" +
                "    \"accountManagerId\": 46,\n" +
                "    \"email\": \"test.test02" + System.currentTimeMillis() + "@example.com\",\n" + // unique email
                "    \"phone\": \"1234567890\",\n" +
                "    \"country\": \"USA\",\n" +
                "    \"address\": \"123 Main St\",\n" +
                "    \"state\": \"Ohio\",\n" +
                "    \"city\": \"Cleveland\",\n" +
                "    \"postalCode\": \"44101\",\n" +
                "    \"phone_code\": \"+1\"\n" +
                "}";

        Response addUserResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(salesUserPayload)
                .when()
                .post("/sales-users/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .response();

        System.out.println("Sales user added successfully.");

        // Parse using JsonPath
        JsonPath jsonPath2 = addUserResponse.jsonPath();

        int salesUserID = jsonPath2.getInt("data.id");
        String message = jsonPath2.getString("message");

        System.out.println("✅ ID: " + salesUserID);
        System.out.println("✅ Message: " + message);

        // Deleting a Sales users
        Response responseDel = given()
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .when()
                .delete("/sales-users/" + salesUserID)
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .response();
        System.out.println(responseDel);

    }
    }



