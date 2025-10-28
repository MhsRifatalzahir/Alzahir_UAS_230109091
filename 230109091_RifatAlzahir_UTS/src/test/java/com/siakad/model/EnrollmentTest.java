package com.siakad.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test untuk class Enrollment, memastikan cakupan penuh
 * pada kedua konstruktor, dan semua getter/setter, termasuk LocalDateTime.
 */
class EnrollmentTest {

    // Data sample
    private final String TEST_ID = "E001";
    private final String TEST_STUDENT_ID = "S005";
    private final String TEST_COURSE_CODE = "MATH101";
    private final LocalDateTime TEST_DATE = LocalDateTime.of(2024, 8, 15, 10, 0);
    private final String TEST_STATUS = "PENDING";

    // Data baru untuk pengujian setter
    private final String NEW_ID = "E002";
    private final LocalDateTime NEW_DATE = LocalDateTime.of(2024, 8, 16, 11, 30);
    private final String NEW_STATUS = "APPROVED";

    /**
     * Menguji konstruktor tanpa argumen (default constructor).
     */
    @Test
    void testDefaultConstructor() {
        Enrollment enrollment = new Enrollment();
        assertNotNull(enrollment, "Objek Enrollment seharusnya tidak null");
    }

    /**
     * Menguji konstruktor dengan semua argumen dan memverifikasi getter.
     */
    @Test
    void testAllArgsConstructorAndGetters() {
        // 1. Inisialisasi menggunakan konstruktor
        Enrollment enrollment = new Enrollment(
                TEST_ID, TEST_STUDENT_ID, TEST_COURSE_CODE,
                TEST_DATE, TEST_STATUS
        );

        // 2. Verifikasi nilai menggunakan semua getter
        assertEquals(TEST_ID, enrollment.getEnrollmentId(), "ID Pendaftaran tidak sesuai");
        assertEquals(TEST_STUDENT_ID, enrollment.getStudentId(), "ID Mahasiswa tidak sesuai");
        assertEquals(TEST_COURSE_CODE, enrollment.getCourseCode(), "Kode Mata Kuliah tidak sesuai");

        // Memastikan objek LocalDateTime sama persis
        assertEquals(TEST_DATE, enrollment.getEnrollmentDate(), "Tanggal Pendaftaran tidak sesuai");

        assertEquals(TEST_STATUS, enrollment.getStatus(), "Status tidak sesuai");
    }

    /**
     * Menguji semua method setter secara eksplisit, mulai dari objek yang dibuat
     * dengan default constructor. Ini menjamin coverage penuh pada semua setter.
     */
    @Test
    void testAllSetters() {
        // Buat objek menggunakan konstruktor default (memastikan coverage)
        Enrollment enrollment = new Enrollment();

        // Atur nilai menggunakan semua setter
        enrollment.setEnrollmentId(NEW_ID);
        enrollment.setStudentId("S006");
        enrollment.setCourseCode("PHYS202");
        enrollment.setEnrollmentDate(NEW_DATE);
        enrollment.setStatus(NEW_STATUS);

        // Verifikasi nilai menggunakan semua getter
        assertEquals(NEW_ID, enrollment.getEnrollmentId(), "Setter EnrollmentId gagal");
        assertEquals("S006", enrollment.getStudentId(), "Setter StudentId gagal");
        assertEquals("PHYS202", enrollment.getCourseCode(), "Setter CourseCode gagal");

        // Verifikasi LocalDateTime
        assertEquals(NEW_DATE, enrollment.getEnrollmentDate(), "Setter EnrollmentDate gagal");

        assertEquals(NEW_STATUS, enrollment.getStatus(), "Setter Status gagal");
    }
}