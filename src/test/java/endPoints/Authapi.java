package endPoints;

import Base.BaseApi;
import Pojo.LoginReq;
import Pojo.LoginRes;
import Pojo.VerifyOtpRes;
import Utils.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;


import static io.restassured.RestAssured.given;

public class Authapi extends BaseApi {
//If your BaseApi already sets the baseURI from config, you don’t need to pass it again in
    private static final RequestSpecification req = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .build();

    public static LoginRes login(String role) {

        LoginReq loginReq = new LoginReq();
        loginReq.setEmail(ConfigReader.getGlobalValue(role + ".email"));
        loginReq.setPassword(ConfigReader.getGlobalValue(role + ".password"));
        loginReq.setRememberMe(false);
        loginReq.setDeviceId(ConfigReader.getGlobalValue(role + ".deviceId"));

        RequestSpecification loginRequest = given().log().all().spec(req)
                .body(loginReq);

        return  loginRequest.when().post("/auth/login").then().extract().response().as(LoginRes.class);

    }

    public static VerifyOtpRes verifyOtp(String role, String otp) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", ConfigReader.getGlobalValue(role + ".email"));
        requestBody.put("otp", otp);
        requestBody.put("deviceId", ConfigReader.getGlobalValue(role + ".deviceId"));
        requestBody.put("rememberMe", false);

        return given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/auth/verify-otp")
                .then()
                .statusCode(200)
                .extract()
                .as(VerifyOtpRes.class);
    }

    public static Response logout(String AccessToken) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("refreshToken", AccessToken);

        return given()
                .log().all()
                .spec(req)
                .body(requestBody)
                .when()
                .post("/auth/logout")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

}
