package services;

import database.JsonDatabaseManager;
import models.*;

import java.util.ArrayList;
import java.util.List;

public class CertificateService {
    private JsonDatabaseManager db;
    private QuizService quizService;


    public CertificateService() {
        this.db = JsonDatabaseManager.getInstance();
        this.quizService = new QuizService();
    }


    public boolean isEligibleForCertificate(String studentId, String courseId) {
        // Check if certificate already exists
        if (certificateExists(studentId, courseId)) {
            return false; // Already has certificate
        }

        // Get course
        Course course = db.getCourseById(courseId);
        if (course == null) {
            return false;
        }

        // Check if student is enrolled
        List<String> enrolledStudents = course.getStudents();
        if (enrolledStudents == null || !enrolledStudents.contains(studentId)) {
            return false;
        }

        // Get lessons
        Lesson[] lessons = course.getLessons();
        if (lessons == null || lessons.length == 0) {
            return false; // No lessons to complete
        }

        // Check all lessons completed
        List<String> completedLessons = db.getCompletedLessons(studentId, courseId);
        if (completedLessons == null || completedLessons.size() < lessons.length) {
            return false;
        }

        // Check all quizzes passed
        if (!hasPassedAllQuizzes(studentId, courseId)) {
            return false;
        }

        return true;
    }

    public double getCompletionPercentage(String studentId, String courseId) {
        Course course = db.getCourseById(courseId);
        if (course == null) {
            return 0;
        }

        Lesson[] lessons = course.getLessons();
        if (lessons == null || lessons.length == 0) {
            return 0;
        }

        List<String> completedLessons = db.getCompletedLessons(studentId, courseId);
        int completed = completedLessons != null ? completedLessons.size() : 0;

        return (completed / (double) lessons.length) * 100;
    }

    public boolean hasPassedAllQuizzes(String studentId, String courseId) {
        Course course = db.getCourseById(courseId);
        if (course == null) {
            return false;
        }

        Lesson[] lessons = course.getLessons();
        if (lessons == null) {
            return true; // No lessons = no quizzes
        }

        for (Lesson lesson : lessons) {
            // Check ALL lessons that have quizzes (not just requiresQuizPass)
            if (lesson.hasQuiz()) {
                if (!quizService.hasPassedQuizForLesson(studentId, lesson.getLessonId())) {
                    return false;
                }
            }
        }

        return true;
    }

    // ==================== CERTIFICATE GENERATION ====================

    public Certificate generateCertificate(String studentId, String courseId) {
        // Check eligibility first
        if (!isEligibleForCertificate(studentId, courseId)) {
            return null;
        }

        // Get student info
        User user = db.getUserById(studentId);
        if (!(user instanceof Student)) {
            return null;
        }
        Student student = (Student) user;

        // Get course info
        Course course = db.getCourseById(courseId);
        if (course == null) {
            return null;
        }

        // Get instructor info
        User instructorUser = db.getUserById(course.getInstructorId());
        String instructorName = instructorUser != null ? instructorUser.getUsername() : "Unknown Instructor";

        // Create certificate
        Certificate certificate = new Certificate(
                studentId,
                courseId,
                student.getUsername(),
                course.getTitle(),
                instructorName
        );

        // Calculate final score (average of all quiz scores)
        double finalScore = calculateFinalScore(studentId, courseId);
        certificate.setFinalScore(finalScore);

        // Set lesson counts
        Lesson[] lessons = course.getLessons();
        int totalLessons = lessons != null ? lessons.length : 0;
        certificate.setTotalLessons(totalLessons);
        certificate.setCompletedLessons(totalLessons); // All completed if eligible

        // Save certificate
        boolean saved = saveCertificate(certificate);

        if (saved) {
            // Update student's certificate list
            List<String> studentCerts = student.getCertificateIds();
            if (studentCerts == null) {
                studentCerts = new ArrayList<>();
            }
            studentCerts.add(certificate.getCertificateId());
            student.setCertificateIds(studentCerts);
            db.updateUser(student);

            return certificate;
        }

        return null;
    }

    public double calculateFinalScore(String studentId, String courseId) {
        Course course = db.getCourseById(courseId);
        if (course == null) {
            return 0;
        }

        Lesson[] lessons = course.getLessons();
        if (lessons == null || lessons.length == 0) {
            return 100; // No lessons = full score
        }

        double totalScore = 0;
        int quizCount = 0;

        for (Lesson lesson : lessons) {
            if (lesson.hasQuiz()) {
                Quiz quiz = db.getQuizById(lesson.getQuizId());
                if (quiz != null) {
                    QuizAttempt bestAttempt = quizService.getBestAttempt(studentId, quiz.getQuizId());
                    if (bestAttempt != null) {
                        totalScore += bestAttempt.getPercentage();
                        quizCount++;
                    }
                }
            }
        }

        if (quizCount == 0) {
            return 100; // No quizzes = full score
        }

        return totalScore / quizCount;
    }

    // ==================== CERTIFICATE MANAGEMENT ====================

    public boolean saveCertificate(Certificate certificate) {
        if (certificate == null || !certificate.isValid()) {
            return false;
        }
        return db.saveCertificate(certificate);
    }

    public Certificate getCertificateById(String certificateId) {
        return db.getCertificateById(certificateId);
    }

    public List<Certificate> getStudentCertificates(String studentId) {
        return db.getStudentCertificates(studentId);
    }

    public Certificate getCertificateForCourse(String studentId, String courseId) {
        return db.getCertificateForCourse(studentId, courseId);
    }

    public boolean certificateExists(String studentId, String courseId) {
        return db.certificateExists(studentId, courseId);
    }

    // ==================== VERIFICATION ====================

    public boolean verifyCertificate(String certificateId) {
        Certificate certificate = db.getCertificateById(certificateId);

        if (certificate == null) {
            return false;
        }

        // Verify student exists
        User student = db.getUserById(certificate.getStudentId());
        if (student == null) {
            return false;
        }

        // Verify course exists
        Course course = db.getCourseById(certificate.getCourseId());
        if (course == null) {
            return false;
        }

        // Certificate is valid
        return true;
    }

    // ==================== EXPORT ====================

    public String exportAsJSON(Certificate certificate) {
        if (certificate == null) {
            return null;
        }

        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"certificateId\": \"").append(certificate.getCertificateId()).append("\",\n");
        json.append("  \"studentName\": \"").append(certificate.getStudentName()).append("\",\n");
        json.append("  \"courseName\": \"").append(certificate.getCourseName()).append("\",\n");
        json.append("  \"instructorName\": \"").append(certificate.getInstructorName()).append("\",\n");
        json.append("  \"issueDate\": \"").append(certificate.getIssueDate()).append("\",\n");
        json.append("  \"finalScore\": ").append(String.format("%.2f", certificate.getFinalScore())).append(",\n");
        json.append("  \"verified\": ").append(verifyCertificate(certificate.getCertificateId())).append("\n");
        json.append("}");

        return json.toString();
    }

    // Optional: PDF export (requires external library)
    public boolean exportAsPDF(Certificate certificate, String filePath) {
        // This would require an external PDF library like iText or Apache PDFBox
        // For now, return false as it's optional
        System.out.println("PDF export not implemented. Use JSON export instead.");
        return false;
    }
}