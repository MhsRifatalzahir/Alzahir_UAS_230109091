package com.siakad.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test untuk class Student, dirancang untuk mencapai 100% coverage
 * pada konstruktor, getter, dan setter.
 */
class StudentTest {

    // Data sample yang akan digunakan untuk pengujian
    private final String TEST_ID = "S001";
    private final String TEST_NAME = "Budi Santoso";
    private final String TEST_EMAIL = "budi.santoso@email.com";
    private final String TEST_MAJOR = "Informatika";
    private final int TEST_SEMESTER = 3;
    private final double TEST_GPA = 3.85;
    private final String TEST_STATUS = "ACTIVE";

    // Data baru untuk pengujian setter
    private final String NEW_ID = "S002";
    private final String NEW_NAME = "Budi Setiawan";
    private final String NEW_EMAIL = "budi.setiawan@email.com";
    private final String NEW_MAJOR = "Sistem Informasi";
    private final int NEW_SEMESTER = 4;
    private final double NEW_GPA = 3.90;
    private final String NEW_STATUS = "PROBATION";


    /**
     * Menguji konstruktor tanpa argumen (default constructor).
     * Penting untuk coverage konstruktor default.
     */
    @Test
    void testDefaultConstructor() {
        Student student = new Student();
        // Memastikan objek berhasil diinisialisasi
        assertNotNull(student, "Objek Student seharusnya tidak null");
    }

    /**
     * Menguji konstruktor dengan semua argumen dan memverifikasi
     * semua getter bekerja dengan benar saat inisialisasi.
     */
    @Test
    void testAllArgsConstructorAndGetters() {
        // 1. Inisialisasi menggunakan konstruktor
        Student student = new Student(
                TEST_ID, TEST_NAME, TEST_EMAIL, TEST_MAJOR,
                TEST_SEMESTER, TEST_GPA, TEST_STATUS
        );

        // 2. Verifikasi nilai menggunakan semua getter
        assertEquals(TEST_ID, student.getStudentId(), "ID Mahasiswa tidak sesuai");
        assertEquals(TEST_NAME, student.getName(), "Nama tidak sesuai");
        assertEquals(TEST_EMAIL, student.getEmail(), "Email tidak sesuai");
        assertEquals(TEST_MAJOR, student.getMajor(), "Jurusan tidak sesuai");
        assertEquals(TEST_SEMESTER, student.getSemester(), "Semester tidak sesuai");

        // Menggunakan delta (0.001) untuk membandingkan double (GPA)
        assertEquals(TEST_GPA, student.getGpa(), 0.001, "IPK tidak sesuai");
        assertEquals(TEST_STATUS, student.getAcademicStatus(), "Status Akademik tidak sesuai");
    }

    /**
     * Menguji semua method setter secara eksplisit, mulai dari objek kosong
     * yang dibuat dengan default constructor.
     * Ini adalah kunci untuk 100% coverage pada semua setter dan constructor default.
     */
    @Test
    void testAllSettersAndGettersWithDefaultConstructor() {
        // Buat objek menggunakan konstruktor default (memastikan coverage)
        Student student = new Student();

        // Atur semua properti menggunakan semua setter (memastikan coverage)
        student.setStudentId(NEW_ID);
        student.setName(NEW_NAME);
        student.setEmail(NEW_EMAIL);
        student.setMajor(NEW_MAJOR);
        student.setSemester(NEW_SEMESTER);
        student.setGpa(NEW_GPA);
        student.setAcademicStatus(NEW_STATUS);

        // Verifikasi semua properti menggunakan semua getter (memastikan coverage)
        assertEquals(NEW_ID, student.getStudentId(), "Setter StudentId gagal");
        assertEquals(NEW_NAME, student.getName(), "Setter Name gagal");
        assertEquals(NEW_EMAIL, student.getEmail(), "Setter Email gagal");
        assertEquals(NEW_MAJOR, student.getMajor(), "Setter Major gagal");
        assertEquals(NEW_SEMESTER, student.getSemester(), "Setter Semester gagal");
        assertEquals(NEW_GPA, student.getGpa(), 0.001, "Setter GPA gagal");
        assertEquals(NEW_STATUS, student.getAcademicStatus(), "Setter AcademicStatus gagal");
    }
}