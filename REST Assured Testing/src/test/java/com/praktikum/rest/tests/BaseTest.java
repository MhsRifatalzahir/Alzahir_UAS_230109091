package com.praktikum.rest.tests;

import com.praktikum.rest.config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeClass;

/**
 * Base test class yang di-extend oleh semua test classes
 * Berisi common setup dan configuration untuk semua tests
 */
public class BaseTest {

    /**
     * Setup method yang di-execute sebelum semua tests dalam
     * class ini
     * Mengkonfigurasi REST Assured dengan base settings
     */
    @BeforeClass
    public void setup()
    {
        // Set base URI untuk semua API requests dalam test
        // class ini
        RestAssured.baseURI = TestConfig.BASE_URL;

        // Enable request dan response logging untuk debugging
        RestAssured.filters(
                new RequestLoggingFilter(), // Log semua
                // request details
                new ResponseLoggingFilter() // Log semua
                // response details
        );

        // Enable detailed logging hanya ketika test validation
        // fails
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        ;

        // Set default headers untuk semua requests
        RestAssured.requestSpecification = RestAssured.given()
                .header("Content-Type", "application/json") // Set content type sebagai JSON
                .header("Accept", "application/json"); // Accept JSON responses
    }

    /**
     * Method helper untuk switch ke ReqRes API
     * Digunakan untuk tests yang membutuhkan authentication
     * features
     */
    protected void useReqResAPI() {
        // Switch base URL ke Reqres API
        RestAssured.baseURI = TestConfig.REQRES_BASE_URL;

        // Update request specification dengan API Key
        RestAssured.requestSpecification = RestAssured.given()
                .header(TestConfig.API_KEY_HEADER,
                        TestConfig.API_KEY) // Add API Key header
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }

    /**
     * Method helper untuk switch kembali ke JSONPlaceholder API
     * JSONPlaceholder lebih reliable untuk basic testing
     */
    protected void useJSONPlaceholderAPI()
    {
        // Switch back ke JSONPlaceholder API
        RestAssured.baseURI = TestConfig.BASE_URL;

        // Update request specification tanpa API Key
        RestAssured.requestSpecification = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }
}
