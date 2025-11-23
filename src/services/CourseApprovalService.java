package services;

import database.JsonDatabaseManager;
import models.Course;
import java.util.List;

public class CourseApprovalService {
    private JsonDatabaseManager dbManager;

    // Constructor
    public CourseApprovalService() {
        this.dbManager = JsonDatabaseManager.getInstance();
    }

    // Approve a course by setting its status to APPROVED
    public boolean approveCourse(String courseId) {
        if (courseId == null || courseId.isEmpty()) {
            return false;
        }

        Course course = dbManager.getCourseById(courseId);
        if (course == null) {
            return false;
        }

        // Update the course status to APPROVED
        return dbManager.updateCourseStatus(courseId, Course.STATUS_APPROVED);
    }

    // Reject a course by setting its status to REJECTED
    public boolean rejectCourse(String courseId) {
        if (courseId == null || courseId.isEmpty()) {
            return false;
        }

        Course course = dbManager.getCourseById(courseId);
        if (course == null) {
            return false;
        }

        // Update the course status to REJECTED
        return dbManager.updateCourseStatus(courseId, Course.STATUS_REJECTED);
    }

    // Get all pending courses
    public List<Course> getPendingCourses() {
        return dbManager.getPendingCourses();
    }

    // Get all approved courses
    public List<Course> getApprovedCourses() {
        return dbManager.getApprovedCourses();
    }

    // Get all rejected courses
    public List<Course> getRejectedCourses() {
        return dbManager.getRejectedCourses();
    }

    // Get courses by a specific status
    public List<Course> getCoursesByStatus(String status) {
        if (status == null || status.isEmpty()) {
            return getPendingCourses(); // Default to pending
        }

        return dbManager.getCoursesByStatus(status);
    }
}