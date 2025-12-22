package com.praktikum.rest.tests;

import com.praktikum.rest.utils.TestDataGenerator;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;
/**
 * Test class untuk advanced API testing scenarios
 * Mengcover data-driven testing, negative testing, performance
 * testing, dll.
 */
public class AdvancedAPITests extends BaseTest {

    /**
     * Setup method untuk setiap test
     */
    @BeforeMethod
    public void setupMethod() {
        useJSONPlaceholderAPI(); // Gunakan JSONPlaceholder
    }

    /**
     * Test untuk validate response headers
     * Important untuk security, caching, dan content
     * negotiation
     */
    @Test
    public void testResponseHeadersValidation() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .header("Content-Type",
                        containsString("application/json")) // Validate JSON content type
                .header("Server", not(emptyOrNullString())) // Validate server info exists
                .header("X-Powered-By",
                        not(emptyOrNullString())) // Validate technology stack
                .header("Content-Encoding", equalTo("gzip")); // Validate compression
    }

    /**
     * Performance test untuk mengukur response time
     * Validates API performance under normal conditions
     */
    @Test
    public void testResponseTimePerformance() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .time(lessThan(3000L)); // Validate response
        // time less than 3 seconds
    }

    /**
     * Data Provider untuk provide multiple user IDs
     * @return Array of user IDs untuk data-driven testing
     */
    @DataProvider(name = "validUserIds")
    public Object[][] provideValidUserIds() {
        return new Object[][]{
                {1}, {2}, {3}, {4}, {5} // Multiple user IDs
                // untuk testing
        };
    }

    /**
     * Data-driven test menggunakan TestNG DataProvider
     * Test multiple scenarios dengan different input data
     */
    @Test(dataProvider = "validUserIds")
    public void testMultipleUsersWithDataProvider(int userId) {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId) // Dynamic
                // user ID dari DataProvider
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(userId)) // Validate
                // ID matches input
                .body("name", not(emptyOrNullString())) // Validate name exists
                .body("email", not(emptyOrNullString())) // Validate email exists
                .body("username", not(emptyOrNullString())); // Validate username exists
    }

    /**
     * Test untuk pagination functionality
     * Validates API behavior dengan query parameters
     */
    @Test
    public void testPaginationFunctionality() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("userId", 1) // Query
                // parameter untuk
                // filter by user
                .when()
                .get("/posts") // GET posts
                // for specific user
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0)) // Validate
                // non-empty response
                .body("[0].userId", equalTo(1)) // Validate
                // first post belongs to user 1
                .body("[0].title", not(emptyOrNullString())) // Validate title exists
                .body("[0].body", not(emptyOrNullString())); // Validate body exists
    }

    /**
     * Test untuk create user menggunakan HashMap
     * Demonstrates different ways to create request body
     */
    @Test
    public void testCreateUserWithHashMap() {
        // Create request body menggunakan HashMap
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "Test User HashMap");
        userData.put("username", "testuser");
        userData.put("email", "test@example.com");

        given()
                .contentType(ContentType.JSON)
                .body(userData) // Pass
                // HashMap sebagai request body
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("Test User HashMap")) // Validate name
                .body("username", equalTo("testuser")) // Validate username
                .body("email", equalTo("test@example.com")) // Validate email
                .body("id", notNullValue()); // Validate ID generated
    }

    /**
     * Test untuk partial update menggunakan PATCH method
     * Validates partial resource updates
     */
    @Test
    public void testUpdateUserWithPartialData() {
        // Partial data untuk update hanya specific fields
        Map<String, Object> partialData = new HashMap<>();
        partialData.put("name", "Updated Name Only"); // Update
        // hanya nama

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .body(partialData) // Only send
                // fields to update
                .when()
                .patch("/users/{id}") // PATCH
                // request untuk partial update
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Name Only")) // Validate nama terupdate
                .body("id", equalTo(1)); // Validate
        // ID unchanged
    }

    /**
     * Data-driven test dengan Java Faker generated data
     * Demonstrates dynamic test data generation
     */
    @Test
    public void testDataDrivenWithFaker() {
        // Generate 2 test data sets dengan Faker
        for (int i = 0; i < 2; i++) {
            Map<String, Object> userData =
                    TestDataGenerator.generateUserData();

            // Print generated data untuk debugging
            System.out.println("Generated User " + (i + 1) + ": "
                    + userData.get("name"));

            given()
                    .contentType(ContentType.JSON)
                    .body(userData)
                    .when()
                    .post("/users")
                    .then()
                    .statusCode(201)
                    .body("name", equalTo(userData.get("name"))) // Validate generated name
                    .body("id", notNullValue()); // Validate ID generated
        }
    }

    /**
     * Test untuk create user dengan Faker JSON string
     * Demonstrates different data format handling
     */
    @Test
    public void testCreateUserWithFakerJson() {
        String userJson = TestDataGenerator.generateUserJson();
        // Get JSON string dari Faker
        System.out.println("Generated JSON: " + userJson); // Log generated JSON

        given()
                .contentType(ContentType.JSON)
                .body(userJson) // Pass JSON
                // string sebagai body
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", not(emptyOrNullString())) // Validate name exists
                .body("id", notNullValue()); // Validate ID generated
    }

    /**
     * Negative test Create user dengan empty body
     * Validates API behavior dengan incomplete data
     */
    @Test
    public void testCreateUserWithEmptyBody() {
        given()
                .contentType(ContentType.JSON)
                .body("{ }") // Empty
                // JSON object
                .when()
                .post("/users")
                .then()
                .statusCode(201); // JSONPlaceholder accepts empty body
    }

    /**
     * Negative test Create user dengan invalid JSON
     * Validates API error handling untuk malformed requests
     */
    @Test
    public void testCreateUserWithInvalidJSON() {
        given()
                .contentType(ContentType.JSON)
                .body(" invalid json)") // Invalid
                // JSON syntax
                .when()
                .post("/users")
                .then()
                .statusCode(500); // JSONPlaceholder returns 500 untuk invalid JSON
    }

    /**
     * Negative test Get user dengan invalid ID format
     * Validates API behavior dengan invalid path parameters
     */
    @Test
    public void testGetUserWithInvalidId() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", "invalid") // Non-
                // numeric ID
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(404); // Not Found
        // untuk invalid ID format
    }

    /**
     * Test untuk nested resources Get posts for specific user
     * Validates API resource relationships
     */
    @Test
    public void testGetPostsForUser() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("userId", 1) // Filter by
                // user ID
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0)) // Validate
                // non-empty response
                .body("[0].userId", equalTo(1)) // Validate
                // all posts belong to user 1
                .body("[0].title", not(emptyOrNullString())) // Validate title exists
                .body("[0].body", not(emptyOrNullString())); // Validate body exists
    }

    /**
     * Negative test Create user dengan null values
     * Validates API handling of null data
     */
    @Test
    public void testCreateUserWithNullValues() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", null); // Null value
        userData.put("username", "testuser");
        userData.put("email", "test@example.com");

        given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post("/users")
                .then()
                .statusCode(201) // JSONPlaceholder handles null values
                .body("username", equalTo("testuser")); // Validate other fields
    }

    /**
     * Boundary test Create user dengan very long name
     * Validates API handling of extreme data values
     */
    @Test
    public void testCreateUserWithVeryLongName() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "A".repeat(500)); // Very long
        // name (500 characters)
        userData.put("username", "longuser");
        userData.put("email", "long@example.com");

        given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post("/users")
                .then()
                .statusCode(201) // JSONPlaceholder accepts long values
                .body("username", equalTo("longuser")); // Validate username
    }
}
