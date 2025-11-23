package models;

import java.util.ArrayList;
import java.util.List;

public class Course {
    // NEW: Approval Status Constants
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";

    private String courseId;
    private String title;
    private String description;
    private String instructorId;
    private Lesson[] lessons;  // Must be array for JsonDatabaseManager
    private List<String> students;  // Must be named "students" for JsonDatabaseManager
    private String approvalStatus; // NEW: "PENDING", "APPROVED", "REJECTED"

    // Default constructor
    public Course() {
        this.students = new ArrayList<>();
        this.lessons = new Lesson[0];
        this.approvalStatus = STATUS_PENDING; // NEW: Default to pending
    }

    public Course(String courseId, String title, String description, String instructorId) {
        this();
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
    }

    // Getters and setters
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }

    public Lesson[] getLessons() { return lessons; }
    public void setLessons(Lesson[] lessons) {
        this.lessons = lessons != null ? lessons : new Lesson[0];
    }

    public List<String> getStudents() { return students; }
    public void setStudents(List<String> students) {
        this.students = students != null ? students : new ArrayList<>();
    }

    // NEW: Approval Status getter and setter
    public String getApprovalStatus() {
        return approvalStatus != null ? approvalStatus : STATUS_PENDING;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    // NEW: Helper methods for status checking
    public boolean isApproved() {
        return STATUS_APPROVED.equals(approvalStatus);
    }

    public boolean isPending() {
        return STATUS_PENDING.equals(approvalStatus) || approvalStatus == null;
    }

    public boolean isRejected() {
        return STATUS_REJECTED.equals(approvalStatus);
    }

    // Business methods
    public void addLesson(Lesson lesson) {
        if (lesson != null) {
            // Check for duplicate
            for (Lesson l : lessons) {
                if (l != null && l.getLessonId().equals(lesson.getLessonId())) {
                    return;
                }
            }

            Lesson[] newLessons = new Lesson[lessons.length + 1];
            System.arraycopy(lessons, 0, newLessons, 0, lessons.length);
            newLessons[lessons.length] = lesson;
            this.lessons = newLessons;
        }
    }

    public void enrollStudent(String studentId) {
        if (studentId != null && !students.contains(studentId)) {
            students.add(studentId);
        }
    }

    public boolean isStudentEnrolled(String studentId) {
        return students.contains(studentId);
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", title='" + title + '\'' +
                ", instructorId='" + instructorId + '\'' +
                ", approvalStatus='" + approvalStatus + '\'' +
                ", lessons=" + (lessons != null ? lessons.length : 0) +
                ", students=" + students.size() +
                '}';
    }
}