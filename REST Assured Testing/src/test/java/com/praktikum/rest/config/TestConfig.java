package com.praktikum.rest.config;

/**
 * Configuration class untuk menyimpan semua constants dan
 * configuration values
 * Digunakan oleh semua test classes untuk menjaga consistency
 */
public class TestConfig {
    // Base URL untuk API testing (menggunakan JSONPlaceholder)
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    // Alternative API URL untuk testing yang butuh
    // authentication (optional)
    public static final String REQRES_BASE_URL = "https://reqres.in/api";

    // API Key untuk ReqRes API (jika diperlukan)
    public static final String API_KEY = "reqres-free-v1";
    public static final String API_KEY_HEADER = "X-API-Key";

    // Test data constants untuk test data yang konsisten
    public static final String VALID_EMAIL = "eve.holt@reqres.in";
    public static final String VALID_PASSWORD = "cityslicka";
    public static final String INVALID_EMAIL = "invalid@test.com";

    // Response time thresholds dalam milliseconds untuk
    // performance testing
    public static final long MAX_RESPONSE_TIME = 3000L; // Maximum acceptable response time
    public static final long ACCEPTABLE_RESPONSE_TIME = 1000L; // Ideal response time

    // Test data paths untuk JSON schema validation (jika
    // digunakan)
    public static final String USERS_SCHEMA_PATH = "schemas/users-schema.json";
    public static final String USER_SCHEMA_PATH = "schemas/user-schema.json";
    public static final String LOGIN_SCHEMA_PATH = "schemas/login-schema.json";
}
