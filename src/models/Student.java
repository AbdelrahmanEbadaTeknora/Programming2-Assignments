package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends User {

    private List<String> enrolledCourses;
    private Map<String, List<String>> progress;
    private List<String> certificateIds;  // NEW: Added attribute

    public Student() {
        super("", "", "", "", "student");
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
        this.certificateIds = new ArrayList<>();  // NEW: Initialize in constructor
    }

    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "student");
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
        this.certificateIds = new ArrayList<>();  // NEW: Initialize in constructor
    }

    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public Map<String, List<String>> getProgress() {
        return progress;
    }

    public void setEnrolledCourses(List<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public void setProgress(Map<String, List<String>> progress) {
        this.progress = progress;
    }

    // NEW: Getter for certificateIds
    public List<String> getCertificateIds() {
        return certificateIds;
    }

    // NEW: Setter for certificateIds
    public void setCertificateIds(List<String> certificateIds) {
        this.certificateIds = certificateIds != null ? certificateIds : new ArrayList<>();
    }

    // NEW: Add a single certificate
    public void addCertificate(String certificateId) {
        if (certificateId != null && !certificateIds.contains(certificateId)) {
            certificateIds.add(certificateId);
        }
    }

    public void enrollInCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
        }
    }

    public void markLessonCompleted(String courseId, String lessonId) {
        progress.putIfAbsent(courseId, new ArrayList<>());
        List<String> completedLessons = progress.get(courseId);

        if (!completedLessons.contains(lessonId)) {
            completedLessons.add(lessonId);
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", enrolledCourses=" + enrolledCourses +
                ", certificateIds=" + certificateIds +  // NEW: Added to toString
                '}';
    }
}