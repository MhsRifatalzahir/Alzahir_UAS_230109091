package com.praktikum.rest.tests;

import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test class untuk authentication related API endpoints
 * Menggunakan mock tests karena JSONPlaceholder tidak punya
 * auth endpoints
 */
public class AuthenticationTests extends BaseTest {
    /**
     * Setup method untuk memastikan menggunakan API yang tepat
     */
    @BeforeMethod
    public void setupMethod() {
        useJSONPlaceholderAPI(); // Gunakan JSONPlaceholder
        // untuk consistency
    }

    /**
     * Mock test untuk simulate successful authentication
     * Menggunakan existing endpoint sebagai proxy untuk auth
     */
    @Test
    public void testSuccessfulLoginMock() {
        // Karena JSONPlaceholder tidak punya login endpoint,
        // kita menggunakan GET user sebagai proxy untuk request
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users/1") // GET existing
                // user sebagai mock login
                .then()
                .statusCode(200) // Validate
                // successful response
                .contentType(ContentType.JSON) // Validate JSON response
                .body("id", equalTo(1)) // Validate user
                // data returned
                .body("name", not(emptyOrNullString())) // Validate name exists
                .body("email", not(emptyOrNullString())); // Validate email exists
    }

    /**
     * Mock test untuk simulate failed authentication
     * Menggunakan non-existent endpoint untuk simulate auth
     * failure
     */
    @Test
    public void testFailedAuthenticationMock() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/nonexistent-endpoint") // Non-existent
                // endpoint untuk mock auth failure
                .then()
                .statusCode(404); // Validate Not
        // Found response
    }

    /**
     * Test untuk create resource tanpa authentication
     * Validates: API behavior tanpa auth credentials
     */
    @Test
    public void testCreatePostWithoutAuth() {
        String postBody = "{\"title\": \"Test Post\", \"body\": \"This is a test post without authentication\", \"userId\": 1}";

        given()
                .contentType(ContentType.JSON)
                .body(postBody)
                .when()
                .post("/posts") // POST request
                // tanpa auth
                .then()
                .statusCode(201) // Validate
                // resource created
                .body("title", equalTo("Test Post")) // Validate title
                .body("body", containsString("test post")) // Validate body content
                .body("userId", equalTo(1)) // Validate user ID
                .body("id", notNullValue()); // Validate ID generated
    }

    /**
     * Test untuk simulate request dengan malformed data
     * Validates: API error handling untuk invalid requests
     */
    @Test
    public void testWithMalformedData() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"invalid\": \"data\"}") // Malformed
                // atau incomplete data
                .when()
                .post("/posts") // POST request
                // dengan invalid data
                .then()
                .statusCode(201) // JSONPlaceholder masih accept data apapun
                .body("id", notNullValue()); //Masih
        // generate ID
    }

    /**
     * Optional test untuk check ReqRes API availability
     * Dengan error handling dengan fungsi skip jika API down
     */
    @Test
    public void testReqResAPIAvailability() {
        try {
            // Switch sementara ke ReqRes API
            useReqResAPI();
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/users") // GET request ke
                    // ReqRes API
                    .then()
                    .statusCode(anyOf(equalTo(200),
                            equalTo(403), equalTo(404))); // Accept multiple status codes
        } finally {
            // Always return to JSONPlaceholder untuk test
            // consistency
            useJSONPlaceholderAPI();
        }
    }

    /**
     * Test untuk validate response headers
     * Important untuk security dan content negotiation
     */
    @Test
    public void testResponseHeaders() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .header("Content-Type",
                        containsString("application/json")) // Validate content type
                .header("Server", not(emptyOrNullString())) // Validate server header
                .header("Cache-Control",
                        not(emptyOrNullString())); // Validate cache header
    }
}
