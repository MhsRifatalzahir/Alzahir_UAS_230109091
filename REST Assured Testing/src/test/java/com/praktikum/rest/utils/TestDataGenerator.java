package com.praktikum.rest.utils;

import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Utility class untuk generate realistic test data menggunakan
 * Java Faker
 * Membantu membuat test data yang variatif dan realistic
 */
public class TestDataGenerator {

    // Initialize Faker dengan Indonesian locale untuk data yang
    // lebih relatable
    private static final Faker faker = new Faker(new Locale("id- ID"));

    /**
     * Generate complete user data dengan semua fields
     * @return Map berisi user data dengan structure yang
     * complete
     */
    public static Map<String, Object> generateUserData() {
        Map<String, Object> userData = new HashMap<>();

        // Basic user information
        userData.put("name", faker.name().fullName()); // Full name
        userData.put("username", faker.name().username().replaceAll("[^a-zA-Z0-9]", "")); // Username tanpa special chars
        userData.put("email", faker.internet().emailAddress()); // Email address
        userData.put("phone", faker.phoneNumber().phoneNumber()); // Phone number
        userData.put("website", faker.internet().url()); // Website URL

        // Address information
        Map<String, Object> address = new HashMap<>();
        address.put("street", faker.address().streetAddress()); // Street address
        address.put("city", faker.address().city()); //City
        address.put("zipcode", faker.address().zipCode()); //ZIP code
        userData.put("address", address);

        // Company information
        Map<String, Object> company = new HashMap<>();
        company.put("name", faker.company().name()); // Company name
        company.put("catchPhrase", faker.company().catchPhrase()); // Company catchphrase
        userData.put("company", company);

        return userData;
    }

    /**
     * Generate company data saja
     * @return Map berisi company data
     */
    public static Map<String, Object> generateCompanyData() {
        Map<String, Object> companyData = new HashMap<>();
        companyData.put("name", faker.company().name()); // Company name
        companyData.put("catchPhrase", faker.company().catchPhrase()); // Company tagline
        companyData.put("bs", faker.company().bs()); // Business statement
        return companyData;
    } // Kurung kurawal penutup yang hilang dari PDF ada di sini

    /**
     * Generate user data dengan address information
     * @return Map berisi user data dengan address
     */
    public static Map<String, Object> generateUserWithAddress() {
        Map<String, Object> userData =
                // Get basic user data
                generateUserData();

        Map<String, Object> address = new HashMap<>();
        address.put("street", faker.address().streetAddress()); // Street
        address.put("city", faker.address().city()); //City
        address.put("zipcode", faker.address().zipCode()); // ZIP code
        userData.put("address", address); // Add address to user data

        return userData;
    }

    /**
     * Generate login credentials (mock)
     * @return Map berisi email dan password
     */
    public static Map<String, Object> generateLoginData() {
        Map<String, Object> loginData = new HashMap<>();
        loginData.put("email", faker.internet().emailAddress()); // Email
        loginData.put("password", faker.internet().password(8, 12, true, true, true)); // Strong password
        return loginData; // Kurung kurawal penutup yang hilang dari PDF ada di sini
    }

    /**
     * Generate user data dalam format JSON string
     * @return JSON string dengan user data
     */
    public static String generateUserJson() {
        return String.format("""
                {
                    "name": "%s",
                    "username": "%s",
                    "email": "%s"
                }
                """,
                faker.name().fullName(), // Name
                faker.name().username().replaceAll("[^a-zA-Z0-9]", ""), // Username
                faker.internet().emailAddress()); // Email
    } // Kurung kurawal penutup yang hilang dari PDF ada di sini

    /**
     * Generate valid user data untuk positive testing
     * @return Map berisi valid user data
     */
    public static Map<String, Object> generateValidUserData() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "Test User " + faker.number().digits(3)); // Test User dengan random digits
        userData.put("username", "user" + faker.number().digits(3)); // Username dengan random digits
        userData.put("email", faker.internet().emailAddress()); // Valid email
        return userData;
    }

    /**
     * Generate product data untuk e-commerce testing
     * @return Map berisi product data
     */
    public static Map<String, Object> generateProductData() {
        Map<String, Object> productData = new HashMap<>();
        productData.put("name", faker.commerce().productName()); // Product name
        productData.put("price", faker.commerce().price()); // Product price
        productData.put("department", faker.commerce().department()); // Department
        productData.put("material", faker.commerce().material()); // Material
        return productData;
    }

    /**
     * Generate multiple users untuk bulk testing
     * @param count Jumlah users yang akan di generate
     * @return Array of user data maps
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object>[]
    generateMultipleUsers(int count) {
        Map<String, Object>[] users = new HashMap[count];
        for (int i = 0; i < count; i++) {
            users[i] = generateUserData(); // Generate untuk masing-masing user
        }
        return users;
    }
}
