package com.siakad.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test untuk class Course, memastikan cakupan penuh
 * pada kedua konstruktor, semua getter, semua setter, dan method addPrerequisite.
 */
class CourseTest {

    // Data sample
    private final String TEST_CODE = "CS101";
    private final String TEST_NAME = "Pengantar Pemrograman";
    private final int TEST_CREDITS = 3;
    private final int TEST_CAPACITY = 40;
    private final int TEST_ENROLLED = 35;
    private final String TEST_LECTURER = "Dr. Anita Dewi";
    private final List<String> TEST_PREREQS = Arrays.asList("SMA001", "SMA002");

    // Data baru untuk pengujian setter
    private final String NEW_CODE = "CS201";
    private final int NEW_CREDITS = 4;
    private final String NEW_PREREQ = "CS101";
    private final List<String> NEW_PREREQS_LIST = Arrays.asList("MA101", "PH101");

    /**
     * Menguji konstruktor tanpa argumen (default constructor).
     */
    @Test
    void testDefaultConstructor() {
        Course course = new Course();
        assertNotNull(course, "Objek Course seharusnya tidak null");
        // Memastikan list prerequisites diinisialisasi, bukan null.
        assertNotNull(course.getPrerequisites(), "Prerequisites list harus diinisialisasi");
        assertTrue(course.getPrerequisites().isEmpty(), "Prerequisites list harus kosong secara default");
    }

    /**
     * Menguji konstruktor dengan argumen utama dan memverifikasi getter.
     */
    @Test
    void testAllArgsConstructorAndGetters() {
        // 1. Inisialisasi menggunakan konstruktor
        Course course = new Course(
                TEST_CODE, TEST_NAME, TEST_CREDITS,
                TEST_CAPACITY, TEST_ENROLLED, TEST_LECTURER
        );

        // 2. Verifikasi nilai menggunakan semua getter
        assertEquals(TEST_CODE, course.getCourseCode(), "Kode MK tidak sesuai");
        assertEquals(TEST_NAME, course.getCourseName(), "Nama MK tidak sesuai");
        assertEquals(TEST_CREDITS, course.getCredits(), "SKS tidak sesuai");
        assertEquals(TEST_CAPACITY, course.getCapacity(), "Kapasitas tidak sesuai");
        assertEquals(TEST_ENROLLED, course.getEnrolledCount(), "Jumlah Terdaftar tidak sesuai");
        assertEquals(TEST_LECTURER, course.getLecturer(), "Dosen tidak sesuai");

        // Memastikan prerequisites tetap kosong setelah konstruktor ini
        assertNotNull(course.getPrerequisites(), "Prerequisites list harus diinisialisasi");
        assertTrue(course.getPrerequisites().isEmpty(), "Prerequisites harus kosong");
    }

    /**
     * Menguji semua method setter, menggunakan default constructor.
     * Ini menjamin coverage penuh pada semua setter.
     */
    @Test
    void testAllSetters() {
        Course course = new Course();

        // Atur nilai menggunakan semua setter
        course.setCourseCode(NEW_CODE);
        course.setCourseName("Algoritma & Struktur Data");
        course.setCredits(NEW_CREDITS);
        course.setCapacity(50);
        course.setEnrolledCount(10);
        course.setLecturer("Prof. Budi");
        course.setPrerequisites(NEW_PREREQS_LIST); // Menguji setter List

        // Verifikasi nilai menggunakan semua getter
        assertEquals(NEW_CODE, course.getCourseCode());
        assertEquals("Algoritma & Struktur Data", course.getCourseName());
        assertEquals(NEW_CREDITS, course.getCredits());
        assertEquals(50, course.getCapacity());
        assertEquals(10, course.getEnrolledCount());
        assertEquals("Prof. Budi", course.getLecturer());
        assertEquals(NEW_PREREQS_LIST, course.getPrerequisites(), "Setter List Prerequisites gagal");
    }

    /**
     * Menguji method addPrerequisite.
     */
    @Test
    void testAddPrerequisite() {
        Course course = new Course();

        // Tambahkan satu prasyarat
        course.addPrerequisite(NEW_PREREQ);

        // Verifikasi
        assertEquals(1, course.getPrerequisites().size(), "Ukuran list harus 1 setelah ditambah");
        assertTrue(course.getPrerequisites().contains(NEW_PREREQ), "Prasyarat harus ada di list");

        // Tambahkan prasyarat lain
        course.addPrerequisite(TEST_CODE);
        assertEquals(2, course.getPrerequisites().size(), "Ukuran list harus 2");
    }
}