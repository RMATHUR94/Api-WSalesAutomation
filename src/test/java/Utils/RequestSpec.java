package Utils;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

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
            File logFile = new File("target/api-log.txt");
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

}


