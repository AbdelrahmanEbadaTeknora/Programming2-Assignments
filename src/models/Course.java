package models;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseId;
    private String title;
    private String description;
    private String instructorId;
    private Lesson[] lessons;  // Must be array for JsonDatabaseManager
    private List<String> students;  // Must be named "students" for JsonDatabaseManager

    // Default constructor
    public Course() {
        this.students = new ArrayList<>();
        this.lessons = new Lesson[0];
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
                ", lessons=" + (lessons != null ? lessons.length : 0) +
                ", students=" + students.size() +
                '}';
    }
}