import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends User {

    private List<String> enrolledCourses;               // List of courseIds
    private Map<String, List<String>> progress;         // courseId -> list of completed lessonIds

    // Constructor
    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "student");

        // Initialize collections so they never cause null errors
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
    }

    // --- Getters ---
    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public Map<String, List<String>> getProgress() {
        return progress;
    }

    // --- Setters ---
    public void setEnrolledCourses(List<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public void setProgress(Map<String, List<String>> progress) {
        this.progress = progress;
    }

    // Add a single course
    public void enrollInCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
        }
    }

    // Mark lesson as completed inside progress map
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
                '}';
    }
}
