package Base;

import io.restassured.RestAssured;
import Utils.ConfigReader;

public class BaseApi {
    static {
        RestAssured.baseURI = ConfigReader.getGlobalValue("baseUrl");
    }
}