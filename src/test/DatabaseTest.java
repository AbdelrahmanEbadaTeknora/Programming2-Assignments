package test;

import database.JsonDatabaseManager;
import models.*;

public class DatabaseTest {
    public static void main(String[] args) {
        JsonDatabaseManager db = JsonDatabaseManager.getInstance();

        System.out.println("=== Testing Database Manager ===\n");

        // Test 1: Create a lesson
        System.out.println("Test 1: Creating lessons...");
        Lesson lesson1 = new Lesson("L001", "Introduction to Java",
                "Java is a high-level programming language...",
                new String[]{"https://docs.oracle.com/javase/"});

        Lesson lesson2 = new Lesson("L002", "Variables and Data Types",
                "Variables are containers for storing data...",
                new String[]{"https://www.w3schools.com/java/"});

        System.out.println("Lesson 1: " + lesson1.getTitle());
        System.out.println("Lesson 2: " + lesson2.getTitle());

        // Test 2: Add lessons to a course (assuming course exists)
        System.out.println("\nTest 2: Adding lessons to course...");
        String testCourseId = "C001"; // You'll need to create this course first

        boolean added1 = db.addLessonToCourse(testCourseId, lesson1);
        boolean added2 = db.addLessonToCourse(testCourseId, lesson2);

        System.out.println("Lesson 1 added: " + added1);
        System.out.println("Lesson 2 added: " + added2);

        // Test 3: Retrieve lessons
        System.out.println("\nTest 3: Retrieving lessons...");
        var lessons = db.getLessonsByCourse(testCourseId);
        System.out.println("Found " + lessons.size() + " lessons:");
        for (Lesson l : lessons) {
            System.out.println("  - " + l.getTitle());
        }

        // Test 4: Mark lesson as completed
        System.out.println("\nTest 4: Marking lesson as completed...");
        String testStudentId = "S001"; // You'll need a valid student ID
        boolean marked = db.markLessonCompleted(testStudentId, testCourseId, "L001");
        System.out.println("Lesson marked as completed: " + marked);

        // Test 5: Get completed lessons
        System.out.println("\nTest 5: Getting completed lessons...");
        var completed = db.getCompletedLessons(testStudentId, testCourseId);
        System.out.println("Completed lessons: " + completed);

        System.out.println("\n=== All tests completed ===");
    }
}