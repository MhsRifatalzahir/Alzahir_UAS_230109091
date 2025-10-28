package com.siakad.service;

import com.siakad.exception.*;
import com.siakad.model.Course;
import com.siakad.model.Enrollment;
import com.siakad.model.Student;
import com.siakad.repository.CourseRepository;
import com.siakad.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    // Dependencies yang akan di-mock
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private GradeCalculator gradeCalculator;

    // Class yang akan diuji, dengan dependency di atas di-inject
    @InjectMocks
    private EnrollmentService enrollmentService;

    // Data Dummy
    private final String VALID_STUDENT_ID = "S123";
    private final String VALID_COURSE_CODE = "CS101";
    private Student validStudent;
    private Course validCourse;

    @BeforeEach
    void setUp() {
        // Setup objek Student dan Course standar untuk digunakan di berbagai test
        validStudent = new Student(VALID_STUDENT_ID, "Budi", "budi@mail.com", "IF", 3, 3.5, "ACTIVE");
        validCourse = new Course(VALID_COURSE_CODE, "Pemrograman Dasar", 3, 40, 30, "A");
    }

    //---------------------------------------------------------
    // TEST METHOD: enrollCourse
    //---------------------------------------------------------

    /**
     * Skenario Sukses: Semua validasi dilewati.
     */
    @Test
    void testEnrollCourse_Success() {
        // Setup Mocking: Ketika method dipanggil, kembalikan objek dummy
        when(studentRepository.findById(VALID_STUDENT_ID)).thenReturn(validStudent);
        when(courseRepository.findByCourseCode(VALID_COURSE_CODE)).thenReturn(validCourse);
        // Prasyarat terpenuhi
        when(courseRepository.isPrerequisiteMet(VALID_STUDENT_ID, VALID_COURSE_CODE)).thenReturn(true);

        // Eksekusi
        Enrollment result = enrollmentService.enrollCourse(VALID_STUDENT_ID, VALID_COURSE_CODE);

        // Verifikasi Output
        assertNotNull(result);
        assertEquals(VALID_STUDENT_ID, result.getStudentId());
        assertEquals(VALID_COURSE_CODE, result.getCourseCode());
        assertEquals("APPROVED", result.getStatus());

        // Verifikasi Interaksi (PENTING untuk Service Layer)
        // 1. Course Enrollment Count di-update (30 -> 31)
        assertEquals(31, validCourse.getEnrolledCount());
        // 2. Repository update dipanggil
        verify(courseRepository, times(1)).update(validCourse);
        // 3. Notifikasi dikirim
        verify(notificationService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    // --- Test Exception enrollCourse ---

    @Test
    void testEnrollCourse_ThrowsStudentNotFoundException() {
        // Setup Mocking: Student tidak ditemukan
        when(studentRepository.findById(anyString())).thenReturn(null);

        // Eksekusi dan Verifikasi Exception
        assertThrows(StudentNotFoundException.class, () ->
                enrollmentService.enrollCourse("S999", VALID_COURSE_CODE));
    }

    @Test
    void testEnrollCourse_ThrowsEnrollmentException_Suspended() {
        // Setup: Student disuspended
        validStudent.setAcademicStatus("SUSPENDED");
        when(studentRepository.findById(VALID_STUDENT_ID)).thenReturn(validStudent);

        // Eksekusi dan Verifikasi Exception
        assertThrows(EnrollmentException.class, () ->
                enrollmentService.enrollCourse(VALID_STUDENT_ID, VALID_COURSE_CODE));
    }

    @Test
    void testEnrollCourse_ThrowsCourseNotFoundException() {
        // Setup: Student valid, Course tidak ditemukan
        when(studentRepository.findById(VALID_STUDENT_ID)).thenReturn(validStudent);
        when(courseRepository.findByCourseCode(VALID_COURSE_CODE)).thenReturn(null);

        // Eksekusi dan Verifikasi Exception
        assertThrows(CourseNotFoundException.class, () ->
                enrollmentService.enrollCourse(VALID_STUDENT_ID, VALID_COURSE_CODE));
    }

    @Test
    void testEnrollCourse_ThrowsCourseFullException() {
        // Setup: Course penuh (EnrolledCount == Capacity)
        validCourse.setEnrolledCount(validCourse.getCapacity()); // 40 = 40
        when(studentRepository.findById(VALID_STUDENT_ID)).thenReturn(validStudent);
        when(courseRepository.findByCourseCode(VALID_COURSE_CODE)).thenReturn(validCourse);

        // Eksekusi dan Verifikasi Exception
        assertThrows(CourseFullException.class, () ->
                enrollmentService.enrollCourse(VALID_STUDENT_ID, VALID_COURSE_CODE));
    }

    @Test
    void testEnrollCourse_ThrowsPrerequisiteNotMetException() {
        // Setup: Prasyarat tidak terpenuhi
        when(studentRepository.findById(VALID_STUDENT_ID)).thenReturn(validStudent);
        when(courseRepository.findByCourseCode(VALID_COURSE_CODE)).thenReturn(validCourse);
        when(courseRepository.isPrerequisiteMet(VALID_STUDENT_ID, VALID_COURSE_CODE)).thenReturn(false);

        // Eksekusi dan Verifikasi Exception
        assertThrows(PrerequisiteNotMetException.class, () ->
                enrollmentService.enrollCourse(VALID_STUDENT_ID, VALID_COURSE_CODE));
    }

    //---------------------------------------------------------
    // TEST METHOD: validateCreditLimit
    //---------------------------------------------------------

    @Test
    void testValidateCreditLimit_Success() {
        // Setup: SKS yang diminta (12) <= SKS Maks (15)
        when(studentRepository.findById(VALID_STUDENT_ID)).thenReturn(validStudent);
        when(gradeCalculator.calculateMaxCredits(validStudent.getGpa())).thenReturn(15);

        // Eksekusi dan Verifikasi
        assertTrue(enrollmentService.validateCreditLimit(VALID_STUDENT_ID, 12));
    }

    @Test
    void testValidateCreditLimit_Failed() {
        // Setup: SKS yang diminta (18) > SKS Maks (15)
        when(studentRepository.findById(VALID_STUDENT_ID)).thenReturn(validStudent);
        when(gradeCalculator.calculateMaxCredits(validStudent.getGpa())).thenReturn(15);

        // Eksekusi dan Verifikasi
        assertFalse(enrollmentService.validateCreditLimit(VALID_STUDENT_ID, 18));
    }

    @Test
    void testValidateCreditLimit_ThrowsStudentNotFound() {
        // Setup: Student tidak ditemukan
        when(studentRepository.findById(anyString())).thenReturn(null);

        // Eksekusi dan Verifikasi Exception
        assertThrows(StudentNotFoundException.class, () ->
                enrollmentService.validateCreditLimit("S999", 12));
    }

    //---------------------------------------------------------
    // TEST METHOD: dropCourse
    //---------------------------------------------------------

    @Test
    void testDropCourse_Success() {
        // Setup Mocking
        validCourse.setEnrolledCount(35); // Initial count
        when(studentRepository.findById(VALID_STUDENT_ID)).thenReturn(validStudent);
        when(courseRepository.findByCourseCode(VALID_COURSE_CODE)).thenReturn(validCourse);

        // Eksekusi
        enrollmentService.dropCourse(VALID_STUDENT_ID, VALID_COURSE_CODE);

        // Verifikasi Interaksi
        // 1. Course Enrollment Count di-update (35 -> 34)
        assertEquals(34, validCourse.getEnrolledCount());
        // 2. Repository update dipanggil
        verify(courseRepository, times(1)).update(validCourse);
        // 3. Notifikasi dikirim
        verify(notificationService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testDropCourse_ThrowsStudentNotFoundException() {
        // Setup Mocking: Student tidak ditemukan
        when(studentRepository.findById(anyString())).thenReturn(null);

        // Eksekusi dan Verifikasi Exception
        assertThrows(StudentNotFoundException.class, () ->
                enrollmentService.dropCourse("S999", VALID_COURSE_CODE));
    }

    @Test
    void testDropCourse_ThrowsCourseNotFoundException() {
        // Setup Mocking: Course tidak ditemukan
        when(studentRepository.findById(VALID_STUDENT_ID)).thenReturn(validStudent);
        when(courseRepository.findByCourseCode(anyString())).thenReturn(null);

        // Eksekusi dan Verifikasi Exception
        assertThrows(CourseNotFoundException.class, () ->
                enrollmentService.dropCourse(VALID_STUDENT_ID, "MK999"));
    }
}