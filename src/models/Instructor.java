package models;

import models.User;

import java.util.ArrayList;
import java.util.List;

public class Instructor extends User {

    private List<String> createdCourses;


    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "instructor");


        this.createdCourses = new ArrayList<>();
    }


    public List<String> getCreatedCourses() {
        return createdCourses;
    }


    public void setCreatedCourses(List<String> createdCourses) {
        this.createdCourses = createdCourses;
    }


    public void addCreatedCourse(String courseId) {
        if (!createdCourses.contains(courseId)) {
            createdCourses.add(courseId);
        }
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createdCourses=" + createdCourses +
                '}';
    }
}
