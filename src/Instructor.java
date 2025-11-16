import java.util.ArrayList;
import java.util.List;

public class Instructor extends User {

    private List<String> createdCourses;   // List of courseIds created by this instructor

    // Constructor
    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "instructor");

        // Initialize list so it never becomes null
        this.createdCourses = new ArrayList<>();
    }

    // --- Getter ---
    public List<String> getCreatedCourses() {
        return createdCourses;
    }

    // --- Setter ---
    public void setCreatedCourses(List<String> createdCourses) {
        this.createdCourses = createdCourses;
    }

    // Add a new course created by this instructor
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
