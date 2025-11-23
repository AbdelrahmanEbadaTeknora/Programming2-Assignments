package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Certificate {
    private String certificateId;
    private String studentId;
    private String courseId;
    private String studentName;
    private String courseName;
    private String instructorName;
    private String issueDate;
    private double finalScore;
    private int totalLessons;
    private int completedLessons;


    public Certificate() {
        this.certificateId = generateCertificateId();
        this.issueDate = getCurrentDate();
    }

    public Certificate(String certificateId, String studentId, String courseId) {
        this.certificateId = certificateId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.issueDate = getCurrentDate();
    }

    public Certificate(String studentId, String courseId, String studentName,
                       String courseName, String instructorName) {
        this.certificateId = generateCertificateId();
        this.studentId = studentId;
        this.courseId = courseId;
        this.studentName = studentName;
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.issueDate = getCurrentDate();
    }


    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }

    public int getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(int totalLessons) {
        this.totalLessons = totalLessons;
    }

    public int getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(int completedLessons) {
        this.completedLessons = completedLessons;
    }


    public String generateCertificateId() {
        String year = String.valueOf(LocalDate.now().getYear());
        String uniquePart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "CERT-" + year + "-" + uniquePart;
    }

    private String getCurrentDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(formatter);
    }

    public String getFormattedIssueDate() {
        try {
            LocalDate date = LocalDate.parse(this.issueDate);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            return date.format(formatter);
        } catch (Exception e) {
            return this.issueDate;
        }
    }

    public String getFormattedScore() {
        return String.format("%.1f%%", this.finalScore);
    }

    public double getCompletionPercentage() {
        if (totalLessons <= 0) {
            return 0;
        }
        return (completedLessons / (double) totalLessons) * 100;
    }

    public boolean isValid() {
        return certificateId != null && !certificateId.isEmpty() &&
                studentId != null && !studentId.isEmpty() &&
                courseId != null && !courseId.isEmpty() &&
                studentName != null && !studentName.isEmpty() &&
                courseName != null && !courseName.isEmpty();
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "certificateId='" + certificateId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", instructorName='" + instructorName + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", finalScore=" + getFormattedScore() +
                '}';
    }
}