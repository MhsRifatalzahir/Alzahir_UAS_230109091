package com.praktikum.rest.tests;

import com.praktikum.rest.utils.TestDataGenerator;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test class khusus untuk testing dengan Faker generated data
 * Demonstrates realistic test data generation untuk
 * comprehensive testing
 */
public class FakerDataTests extends BaseTest {

    @BeforeMethod
    public void setupMethod() {
        useJSONPlaceholderAPI();
    }

    /**
     * Test create user dengan Faker generated data
     * Demonstrates realistic data generation untuk testing
     */
    @Test
    public void testCreateUserWithFakerData() {
        // Generate random user data menggunakan Java Faker
        Map<String, Object> userData =
                TestDataGenerator.generateUserData();

        // Print generated data untuk transparency
        System.out.println("Generated User Data:");
        System.out.println("Name: " + userData.get("name"));
        System.out.println("Username: " +
                userData.get("username"));
        System.out.println("Email: " + userData.get("email"));

        given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", equalTo(userData.get("name"))) // Validate generated name
                .body("username",
                        equalTo(userData.get("username"))) // Validate generated username
                .body("email", equalTo(userData.get("email"))) // Validate generated email
                .body("id", notNullValue()); // Validate server-generated ID
    }

    /**
     * Test create multiple users dengan different Faker data
     * Demonstrates data variation dalam testing
     */
    @Test
    public void testCreateMultipleUsersWithFaker() {
        // Test dengan 2 user berbeda untuk demonstrate data
        // variety
        for (int i = 0; i < 2; i++) {
            Map<String, Object> userData =
                    TestDataGenerator.generateUserData();
            System.out.println("Creating user " + (i + 1) + ":" +
                    userData.get("name"));

            given()
                    .contentType(ContentType.JSON)
                    .body(userData)
                    .when()
                    .post("/users")
                    .then()
                    .statusCode(201)
                    .body("name", equalTo(userData.get("name"))) // Validate name
                    .body("username",
                            equalTo(userData.get("username"))) // Validate username
                    .body("id", notNullValue()); // Validate ID
        }
    }

    /**
     * Test dengan Indonesian localized data
     * Demonstrates locale-specific data generation
     */
    @Test
    public void testCreateUserWithIndonesianData() {
        Map<String, Object> userData =
                TestDataGenerator.generateUserData();

        // Log generated Indonesian data
        String userName = (String) userData.get("name");
        String userEmail = (String) userData.get("email");
        System.out.println("Indonesian User Data:");
        System.out.println("Nama: " + userName);
        System.out.println("Email: " + userEmail);

        given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", equalTo(userName)) // Validate
                // Indonesian name
                .body("email", equalTo(userEmail)); // Validate
        // email
    }

    /**
     * Test create user dengan company data
     * Demonstrates complex nested data structures
     */
    @Test
    public void testCreateUserWithCompanyData() {
        Map<String, Object> userData =
                TestDataGenerator.generateUserData();
        Map<String, Object> companyData =
                TestDataGenerator.generateCompanyData();

        // Combine user and company data
        userData.put("company", companyData);

        // Log the combined data
        System.out.println("User with Company Data:");
        System.out.println("User: " + userData.get("name"));
        System.out.println("Company: " +
                companyData.get("name"));
        System.out.println("Catchphrase: " +
                companyData.get("catchPhrase"));

        given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", equalTo(userData.get("name"))) // Validate user name
                .body("company.name",
                        equalTo(companyData.get("name"))); // Validate company name
    }

    /**
     * Test dengan specifically formatted valid data
     * Demonstrates controlled data generation
     */
    @Test
    public void testCreateUserWithValidData() {
        Map<String, Object> userData =
                TestDataGenerator.generateValidUserData();
        System.out.println("Valid User Data:");
        System.out.println("Name: " + userData.get("name"));
        System.out.println("Username: " +
                userData.get("username"));
        System.out.println("Email: " + userData.get("email"));

        given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", equalTo(userData.get("name"))) // Validate formatted name
                .body("username",
                        equalTo(userData.get("username"))) // Validate formatted username
                .body("email", equalTo(userData.get("email"))) // Validate email
                .body("id", notNullValue()); // Validate ID
    }

    /**
     * Test create user dengan complete address information
     * Demonstrates nested object testing
     */
    @Test
    public void testCreateUserWithAddress() {
        Map<String, Object> userData =
                TestDataGenerator.generateUserWithAddress();

        // Extract address untuk validation
        @SuppressWarnings("unchecked")
        Map<String, Object> address = (Map<String, Object>)
                userData.get("address");

        System.out.println("User with Address:");
        System.out.println("Name: " + userData.get("name"));
        System.out.println("Street: " + address.get("street"));
        System.out.println("City: " + address.get("city"));

        given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", equalTo(userData.get("name"))) // Validate user name
                .body("address.street",
                        equalTo(address.get("street"))) // Validate street
                .body("address.city",
                        equalTo(address.get("city"))); // Validate city
    }

    /**
     * Test berbagai variasi data structure dengan Faker
     * Demonstrates comprehensive data variation testing
     */
    @Test
    public void testDataVariationWithFaker() {
        // Test berbagai variasi data structure
        String[] testTypes = {"basic", "with-address", "with- company"};

        for (String testType : testTypes) {
            Map<String, Object> userData;
            // Generate different data structures based on test
            // type
            switch (testType) {
                case "with-address":
                    userData =
                            TestDataGenerator.generateUserWithAddress();
                    break;
                case "with-company":
                    userData =
                            TestDataGenerator.generateUserData();
                    // Ensure company data exists
                    if (!userData.containsKey("company")) {
                        userData.put("company",
                                TestDataGenerator.generateCompanyData());
                    }
                    break;
                default:
                    userData =
                            TestDataGenerator.generateUserData();
            }

            System.out.println("Test Type: " + testType);
            System.out.println("User: " + userData.get("name"));

            given()
                    .contentType(ContentType.JSON)
                    .body(userData)
                    .when()
                    .post("/users")
                    .then()
                    .statusCode(201)
                    .body("name", not(emptyOrNullString())) // Validate name exists
                    .body("id", notNullValue()); // Validate ID generated
        }
    }
}
