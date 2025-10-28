package com.siakad.service;

import com.siakad.model.CourseGrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test untuk class GradeCalculator, menguji semua logika perhitungan
 * IPK, batas SKS, dan penentuan status akademik.
 */
class GradeCalculatorTest {

    private GradeCalculator calculator;

    // Toleransi untuk perbandingan double (IPK dibulatkan 2 desimal)
    private final double DELTA = 0.001;

    @BeforeEach
    void setUp() {
        calculator = new GradeCalculator();
    }

    //---------------------------------------------------------
    // TEST METHOD: calculateGPA
    //---------------------------------------------------------

    /**
     * Menguji perhitungan IPK dasar.
     * Total Poin = (4.0*3) + (3.0*2) = 12.0 + 6.0 = 18.0
     * Total SKS = 3 + 2 = 5
     * IPK = 18.0 / 5 = 3.60
     */
    @Test
    void testCalculateGPA_Basic() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("MK1", 3, 4.0), // A, 3 SKS
                new CourseGrade("MK2", 2, 3.0)  // B, 2 SKS
        );

        double gpa = calculator.calculateGPA(grades);
        // Memastikan hasil pembulatan (3.60) sesuai
        assertEquals(3.60, gpa, DELTA, "IPK perhitungan dasar salah");
    }

    /**
     * Menguji perhitungan dengan hasil yang memerlukan pembulatan ke atas.
     * Total Poin = (4.0*4) + (2.0*1) = 16.0 + 2.0 = 18.0
     * Total SKS = 4 + 1 = 5
     * IPK = 18.0 / 5 = 3.60 (Contoh ini tidak membingungkan, jadi gunakan yang lain)
     */
    @Test
    void testCalculateGPA_Rounding() {
        // Total Poin = (3.0*3) + (4.0*3) = 9 + 12 = 21
        // Total SKS = 3 + 3 = 6
        // IPK = 21 / 6 = 3.50. (Uji kasus yang hasilnya sulit dibulatkan)
        // Coba: (4.0*3) + (2.0*5) = 12 + 10 = 22. Total SKS = 8. IPK = 22/8 = 2.75
        // Coba: (3.0*3) + (2.0*2) = 9 + 4 = 13. Total SKS = 5. IPK = 13/5 = 2.6

        // Kasus: (4.0*3) + (3.0*4) + (2.0*1) = 12 + 12 + 2 = 26. Total SKS = 8. IPK = 3.25
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("MK1", 1, 3.33), // 3.33 points
                new CourseGrade("MK2", 3, 3.0)  // 9.0 points
        );
        // Total Poin: 12.33. Total SKS: 4. IPK: 12.33 / 4 = 3.0825. Dibulatkan -> 3.08

        double gpa = calculator.calculateGPA(grades);
        assertEquals(3.08, gpa, DELTA, "Pembulatan IPK salah (seharusnya 3.08)");
    }

    @Test
    void testCalculateGPA_EdgeCases() {
        // Case: List kosong
        assertEquals(0.0, calculator.calculateGPA(Collections.emptyList()), DELTA, "IPK list kosong harus 0.0");
        // Case: List null
        assertEquals(0.0, calculator.calculateGPA(null), DELTA, "IPK list null harus 0.0");
        // Case: Total SKS nol (list berisi mata kuliah 0 SKS)
        List<CourseGrade> zeroCreditGrades = Collections.singletonList(new CourseGrade("MK_0", 0, 4.0));
        assertEquals(0.0, calculator.calculateGPA(zeroCreditGrades), DELTA, "IPK SKS nol harus 0.0");
    }

    @Test
    void testCalculateGPA_ThrowsInvalidGradePoint() {
        List<CourseGrade> invalidGrades = Collections.singletonList(new CourseGrade("MK_inv", 3, 4.1));
        // Grade Point > 4.0
        assertThrows(IllegalArgumentException.class, () ->
                calculator.calculateGPA(invalidGrades), "Harus melempar exception untuk Grade > 4.0");

        List<CourseGrade> invalidGrades2 = Collections.singletonList(new CourseGrade("MK_inv2", 3, -0.1));
        // Grade Point < 0
        assertThrows(IllegalArgumentException.class, () ->
                calculator.calculateGPA(invalidGrades2), "Harus melempar exception untuk Grade < 0");
    }

    //---------------------------------------------------------
    // TEST METHOD: determineAcademicStatus
    //---------------------------------------------------------

    // --- Uji Semester 1-2 ---
    @Test
    void testDetermineAcademicStatus_Sem1_Active() {
        assertEquals("ACTIVE", calculator.determineAcademicStatus(2.0, 1), "Sem 1, IPK 2.0 harus ACTIVE");
    }

    @Test
    void testDetermineAcademicStatus_Sem2_Probation() {
        assertEquals("PROBATION", calculator.determineAcademicStatus(1.99, 2), "Sem 2, IPK 1.99 harus PROBATION");
    }

    // --- Uji Semester 3-4 ---
    @Test
    void testDetermineAcademicStatus_Sem3_Active() {
        assertEquals("ACTIVE", calculator.determineAcademicStatus(2.25, 3), "Sem 3, IPK 2.25 harus ACTIVE");
    }

    @Test
    void testDetermineAcademicStatus_Sem4_Probation() {
        assertEquals("PROBATION", calculator.determineAcademicStatus(2.24, 4), "Sem 4, IPK 2.24 harus PROBATION");
        assertEquals("PROBATION", calculator.determineAcademicStatus(2.0, 3), "Sem 3, IPK 2.0 harus PROBATION");
    }

    @Test
    void testDetermineAcademicStatus_Sem4_Suspended() {
        assertEquals("SUSPENDED", calculator.determineAcademicStatus(1.99, 4), "Sem 4, IPK 1.99 harus SUSPENDED");
    }

    // --- Uji Semester 5+ ---
    @Test
    void testDetermineAcademicStatus_Sem5_Active() {
        assertEquals("ACTIVE", calculator.determineAcademicStatus(2.5, 5), "Sem 5, IPK 2.5 harus ACTIVE");
    }

    @Test
    void testDetermineAcademicStatus_Sem5_Probation() {
        assertEquals("PROBATION", calculator.determineAcademicStatus(2.49, 5), "Sem 5, IPK 2.49 harus PROBATION");
        assertEquals("PROBATION", calculator.determineAcademicStatus(2.0, 6), "Sem 6, IPK 2.0 harus PROBATION");
    }

    @Test
    void testDetermineAcademicStatus_Sem5_Suspended() {
        assertEquals("SUSPENDED", calculator.determineAcademicStatus(1.99, 5), "Sem 5, IPK 1.99 harus SUSPENDED");
    }

    @Test
    void testDetermineAcademicStatus_ThrowsInvalid() {
        // Invalid GPA
        assertThrows(IllegalArgumentException.class, () ->
                calculator.determineAcademicStatus(4.1, 1), "Harus melempar exception GPA > 4.0");
        // Invalid Semester
        assertThrows(IllegalArgumentException.class, () ->
                calculator.determineAcademicStatus(3.0, 0), "Harus melempar exception Semester < 1");
    }

    //---------------------------------------------------------
    // TEST METHOD: calculateMaxCredits
    //---------------------------------------------------------

    // --- Uji Batas Atas (24 SKS) ---
    @Test
    void testCalculateMaxCredits_24() {
        assertEquals(24, calculator.calculateMaxCredits(3.0), "IPK 3.0 harus 24 SKS");
        assertEquals(24, calculator.calculateMaxCredits(4.0), "IPK 4.0 harus 24 SKS");
    }

    // --- Uji Batas 21 SKS ---
    @Test
    void testCalculateMaxCredits_21() {
        assertEquals(21, calculator.calculateMaxCredits(2.99), "IPK 2.99 harus 21 SKS");
        assertEquals(21, calculator.calculateMaxCredits(2.5), "IPK 2.5 harus 21 SKS");
    }

    // --- Uji Batas 18 SKS ---
    @Test
    void testCalculateMaxCredits_18() {
        assertEquals(18, calculator.calculateMaxCredits(2.49), "IPK 2.49 harus 18 SKS");
        assertEquals(18, calculator.calculateMaxCredits(2.0), "IPK 2.0 harus 18 SKS");
    }

    // --- Uji Batas Bawah (15 SKS) ---
    @Test
    void testCalculateMaxCredits_15() {
        assertEquals(15, calculator.calculateMaxCredits(1.99), "IPK 1.99 harus 15 SKS");
        assertEquals(15, calculator.calculateMaxCredits(0.0), "IPK 0.0 harus 15 SKS");
    }

    @Test
    void testCalculateMaxCredits_ThrowsInvalid() {
        assertThrows(IllegalArgumentException.class, () ->
                calculator.calculateMaxCredits(4.1), "Harus melempar exception GPA > 4.0");
    }
}