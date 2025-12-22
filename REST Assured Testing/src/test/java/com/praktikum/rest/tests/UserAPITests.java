package com.praktikum.rest.tests;

import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test class untuk basic CRUD operations pada User API
 * Mengcover GET, POST, PUT, DELETE methods
 */
public class UserAPITests extends BaseTest {

    /**
     * Setup method yang di-execute sebelum setiap test method
     * Memastikan menggunakan JSONPlaceholder API untuk
     * consistency
     */
    @BeforeMethod
    public void setupMethod() {
        useJSONPlaceholderAPI(); // Pastikan menggunakan
        // JSONPlaceholder
    }

    /**
     * Test untuk GET all users endpoint
     * Validates: status code, response structure, dan data
     * completeness
     */
    @Test
    public void testGetAllUsers() {
        given()
                // Start building the request
                .contentType(ContentType.JSON) // Set content type sebagai JSON
                .when()
                // Specify the HTTP action
                .get("/users") // GET request ke /users endpoint
                .then()
                // Start response validation
                .statusCode(200) // Validate status code is 200 OK
                .contentType(ContentType.JSON) // Validate content type is JSON
                .body("size()", greaterThan(0)) // Validate response array tidak empty
                .body("[0].id", notNullValue()) // Validate first user has ID
                .body("[0].name", not(emptyOrNullString())) // Validate name exists
                .body("[0].email", not(emptyOrNullString())) // Validate email exists
                .body("[0].username", not(emptyOrNullString())); // Validate username exists
    }

    /**
     * Test untuk GET user by ID endpoint
     * Validates: specific user data, field values, dan response
     * structure
     */
    @Test
    public void testGetUserById() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1) // Set path
                // parameter (id) menjadi 1
                .when()
                // Specify the HTTP action
                .get("/users/{id}") // GET
                // request ke /users/1
                .then()
                .statusCode(200) // Validate status code 200
                .contentType(ContentType.JSON) // Validate content type
                .body("id", equalTo(1)) // Validate user ID
                // adalah 1
                .body("name", equalTo("Leanne Graham")) // Validate nama user
                .body("email", equalTo("Sincere@april.biz")) // Validate email user
                .body("username", equalTo("Bret")); // Validate username
    }

    /**
     * Test untuk GET non-existent user
     * Validates: error handling untuk resource yang tidak ada
     */
    @Test
    public void testGetUserNotFound() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", 999) // Use non-
                // existent user ID
                .when()
                .get("/users/{id}") // GET request ke
                // /users/999
                .then()
                .statusCode(404); // Validate status
        // code 404 Not Found
    }

    /**
     * Test untuk POST create new user endpoint
     * Validates: resource creation, response data, dan field
     * values
     */
    @Test
    public void testCreateUser() {
        // Request body sebagai JSON string
        String requestBody = """
                {
                    "name": "John Doe",
                    "username": "johndoe",
                    "email": "john.doe@example.com",
                    "address": {
                        "street": "123 Main St",
                        "city": "Anytown"
                    },
                    "phone": "1-555-123-4567",
                    "website": "johndoe.com",
                    "company": {
                        "name": "ABC Company",
                        "catchPhrase": "Best company ever"
                    }
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody) // Set request
                // body
                .when()
                .post("/users") // POST request ke
                // /users endpoint
                .then()
                .statusCode(201) // Validate
                // status code 201
                // Created
                .contentType(ContentType.JSON) // Validate
                // content type
                .body("name", equalTo("John Doe")) // Validate nama dalam response
                .body("username", equalTo("johndoe")) // Validate username dalam response
                .body("email", equalTo("john.doe@example.com")) // Validate email dalam response
                .body("id", notNullValue()); // Validate ID
        // generated oleh server
    }

    /**
     * Test untuk PUT update user endpoint
     * Validates: resource update, field changes, dan response
     * data
     */
    @Test
    public void testUpdateUser() {
        // Request body dengan updated data
        String requestBody = """
                {
                    "name": "John Updated",
                    "username": "johnupdated",
                    "email": "john.updated@example.com"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1) // User ID yang
                // akan di-update
                .body(requestBody) // Updated
                // data
                .when()
                .put("/users/{id}") // PUT request
                // ke /users/1
                .then()
                .statusCode(200) // Validate
                // status code 200 OK
                .contentType(ContentType.JSON) // Validate
                // content type
                .body("name", equalTo("John Updated")) // Validate nama terupdate
                .body("username", equalTo("johnupdated")) // Validate username terupdate
                .body("email",
                        equalTo("john.updated@example.com")) // Validate email terupdate
                .body("id", equalTo(1)); // Validate ID tetap sama
    }

    /**
     * Test untuk DELETE user endpoint
     * Validates: resource deletion dan response structure
     */
    @Test
    public void testDeleteUser() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1) // User ID yang
                // akan di-delete
                .when()
                .delete("/users/{id}") // DELETE request
                // ke /users/1
                .then()
                .statusCode(200); // Validate status
        // code 200 OK
    }
}
