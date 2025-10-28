package com.siakad.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test untuk class CourseGrade, dirancang untuk mencapai 100% coverage
 * pada konstruktor, getter, dan setter.
 */
class CourseGradeTest {

    // Data sample yang akan digunakan untuk pengujian
    private final String TEST_CODE = "IF001";
    private final int TEST_CREDITS = 3;
    private final double TEST_GRADE_POINT = 4.0; // Nilai A

    // Data baru untuk pengujian setter
    private final String NEW_CODE = "IF002";
    private final int NEW_CREDITS = 4;
    private final double NEW_GRADE_POINT = 3.5; // Nilai AB (contoh)

    /**
     * Menguji konstruktor tanpa argumen (default constructor).
     */
    @Test
    void testDefaultConstructor() {
        CourseGrade courseGrade = new CourseGrade();
        // Memastikan objek berhasil diinisialisasi
        assertNotNull(courseGrade, "Objek CourseGrade seharusnya tidak null");
    }

    /**
     * Menguji konstruktor dengan semua argumen dan memverifikasi
     * semua getter bekerja dengan benar saat inisialisasi.
     */
    @Test
    void testAllArgsConstructorAndGetters() {
        // 1. Inisialisasi menggunakan konstruktor
        CourseGrade courseGrade = new CourseGrade(TEST_CODE, TEST_CREDITS, TEST_GRADE_POINT);

        // 2. Verifikasi nilai menggunakan semua getter
        assertEquals(TEST_CODE, courseGrade.getCourseCode(), "Kode Mata Kuliah tidak sesuai");
        assertEquals(TEST_CREDITS, courseGrade.getCredits(), "SKS tidak sesuai");

        // Menggunakan delta (0.001) untuk membandingkan double (GradePoint)
        assertEquals(TEST_GRADE_POINT, courseGrade.getGradePoint(), 0.001, "Nilai Poin tidak sesuai");
    }

    /**
     * Menguji semua method setter secara eksplisit dengan mengubah
     * nilai dari objek yang dibuat menggunakan konstruktor default.
     * Ini adalah kunci untuk 100% coverage pada semua setter.
     */
    @Test
    void testSettersAndGettersWithDefaultConstructor() {
        // Buat objek menggunakan konstruktor default
        CourseGrade courseGrade = new CourseGrade();

        // Atur semua properti menggunakan semua setter
        courseGrade.setCourseCode(NEW_CODE);
        courseGrade.setCredits(NEW_CREDITS);
        courseGrade.setGradePoint(NEW_GRADE_POINT);

        // Verifikasi semua properti menggunakan semua getter
        assertEquals(NEW_CODE, courseGrade.getCourseCode(), "Setter CourseCode gagal");
        assertEquals(NEW_CREDITS, courseGrade.getCredits(), "Setter Credits gagal");
        assertEquals(NEW_GRADE_POINT, courseGrade.getGradePoint(), 0.001, "Setter GradePoint gagal");
    }
}