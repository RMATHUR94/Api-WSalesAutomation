package Utils;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Holds a single reusable RequestSpecification for the entire project.
 */
public class RequestSpec {

    // Build the reusable spec ONCE
    private static final RequestSpecification spec;

    static {
        PrintStream logStream;
        try {
            // ✅ Create/overwrite the log file (inside target/logs or any folder you like)
            String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File logFile = new File("target/api-log-" + time + ".txt");
            //File logFile = new File("target/api-log.txt");
            logFile.getParentFile().mkdirs();  // ensure folder exists
           // logStream = new PrintStream(new FileOutputStream(logFile, true)); // its appeding whole one by one every logs.
            logStream = new PrintStream(new FileOutputStream(logFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to create log file for RestAssured", e);
        }

        // ✅ Build the reusable spec with file logging
        spec = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.getGlobalValue("baseUrl"))
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter(logStream))   // write request logs to file
                .addFilter(new ResponseLoggingFilter(logStream))  // write response logs to file
                .build();
    }

    public static RequestSpecification get() {
        if (spec == null) {
            throw new IllegalStateException("RequestSpec not initialized or null");
        }
        return spec;
    }

    /**
     * Centralized base request with Authorization and JSON content-type.
     * Use this for most API calls that need Bearer token + base spec.
     */
    public static RequestSpecification baseRequest(String token) {
        // We start from the already-built spec so it keeps the file-logging filters
        return RestAssured.given()
                .spec(get())                                 // keep baseUri + filters + contentType
                .header("Authorization", "Bearer " + token)  // dynamic auth header
                .contentType(ContentType.JSON)               // explicit JSON (overrides if needed)
                .log().all();                                // optional extra console log
    }

    /**
     * Request builder for Swell storefront (different baseUri).
     * Use when you must hit the amplifyapp host in your steps.
     */
    public static RequestSpecification swellRequest() {
        return RestAssured.given()
                .baseUri("https://dev.d35iy77kbiv1w7.amplifyapp.com")
                .contentType(ContentType.JSON)
                .log().all();
    }

    /**
     * Optional helper to post JSON and extract response in one go.
     * Keeps step defs slimmer.
     */
    public static io.restassured.response.Response postJson(String endpoint, String token, String body) {
        return baseRequest(token)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

}


