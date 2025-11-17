package test;

import database.JsonDatabaseManager;
import models.*;
import utils.AuthManager;
import java.util.List;

    public class IntegrationTest {
        private static JsonDatabaseManager db = JsonDatabaseManager.getInstance();

        public static void main(String[] args) {
            System.out.println("=== SKILL FORGE INTEGRATION TEST ===\n");

            // Step 1: Setup test data
            System.out.println("Step 1: Creating test data...");
            setupTestData();

            // Step 2: Test Instructor workflow
            System.out.println("\nStep 2: Testing Instructor workflow...");
            testInstructorWorkflow();

            // Step 3: Test Student workflow
            System.out.println("\nStep 3: Testing Student workflow...");
            testStudentWorkflow();

            System.out.println("\n=== INTEGRATION TEST COMPLETED ===");
        }

        private static void setupTestData() {
            // Create instructor
            Instructor instructor = new Instructor();
            instructor.setUserId("I001");
            instructor.setRole("Instructor");
            instructor.setUsername("Dr. Layla");
            instructor.setEmail("layla@skillforge.com");
            instructor.setPasswordHash(AuthManager.hashPassword("instructor123"));

            if (!db.userExists("I001")) {
                db.saveUser(instructor);
                System.out.println("  ✓ Instructor created");
            } else {
                System.out.println("  ✓ Instructor already exists");
            }

            // Create students
            String[] studentIds = {"S001", "S002", "S003"};
            String[] studentNames = {"Ahmed", "Sara", "Omar"};

            for (int i = 0; i < studentIds.length; i++) {
                if (!db.userExists(studentIds[i])) {
                    Student student = new Student();
                    student.setUserId(studentIds[i]);
                    student.setRole("Student");
                    student.setUsername(studentNames[i]);
                    student.setEmail(studentNames[i].toLowerCase() + "@skillforge.com");
                    student.setPasswordHash(AuthManager.hashPassword("student123"));
                    db.saveUser(student);
                    System.out.println("  ✓ Student " + studentNames[i] + " created");
                } else {
                    System.out.println("  ✓ Student " + studentNames[i] + " already exists");
                }
            }
        }

        private static void testInstructorWorkflow() {
            // Create course
            if (!db.courseExists("C001")) {
                Course course = new Course();
                course.setCourseId("C001");
                course.setTitle("Programming II");
                course.setDescription("Advanced Java Programming");
                course.setInstructorId("I001");
                db.saveCourse(course);
                System.out.println("  ✓ Course created");
            } else {
                System.out.println("  ✓ Course already exists");
            }

            // Add lessons
            List<Lesson> existingLessons = db.getLessonsByCourse("C001");
            if (existingLessons.isEmpty()) {
                Lesson[] lessons = {
                        new Lesson("L001", "OOP Basics", "Introduction to Object-Oriented Programming",
                                new String[]{"https://docs.oracle.com"}),
                        new Lesson("L002", "Inheritance", "Understanding inheritance in Java",
                                new String[]{"https://java.com"}),
                        new Lesson("L003", "Polymorphism", "Polymorphism concepts and examples",
                                new String[]{"https://java.com"})
                };

                for (Lesson lesson : lessons) {
                    db.addLessonToCourse("C001", lesson);
                    System.out.println("  ✓ Lesson added: " + lesson.getTitle());
                }
            } else {
                System.out.println("  ✓ Lessons already exist (" + existingLessons.size() + " lessons)");
            }
        }

        private static void testStudentWorkflow() {
            String studentId = "S001";
            String courseId = "C001";

            // Enroll student
            db.enrollStudent(studentId, courseId);
            System.out.println("  ✓ Student enrolled in course");

            // Get student info
            User user = db.getUserById(studentId);
            if (user instanceof Student) {
                Student student = (Student) user;
                System.out.println("  ✓ Student enrolled courses: " + student.getEnrolledCourses());
            }

            // Complete lessons
            String[] lessonIds = {"L001", "L002"};
            for (String lessonId : lessonIds) {
                db.markLessonCompleted(studentId, courseId, lessonId);
                System.out.println("  ✓ Lesson " + lessonId + " marked complete");
            }

            // Check progress
            List<String> completed = db.getCompletedLessons(studentId, courseId);
            List<Lesson> allLessons = db.getLessonsByCourse(courseId);
            double progress = (completed.size() / (double) allLessons.size()) * 100;

            System.out.printf("  ✓ Progress: %d/%d lessons (%.1f%%)\n",
                    completed.size(), allLessons.size(), progress);
        }
    }