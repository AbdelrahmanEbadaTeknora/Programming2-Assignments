package database;

import models.Instructor;
import models.Course;
import models.Lesson;
import models.Student;
import models.User;
import org.json.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JsonDatabaseManager {
    private static final String USERS_FILE = "users.json";
    private static final String COURSES_FILE = "courses.json";

    // Singleton pattern (optional but recommended)
    private static JsonDatabaseManager instance;

    private JsonDatabaseManager() {
        initializeFiles();
    }

    public static JsonDatabaseManager getInstance() {
        if (instance == null) {
            instance = new JsonDatabaseManager();
        }
        return instance;
    }

    // Initialize JSON files if they don't exist
    private void initializeFiles() {
        try {
            if (!Files.exists(Paths.get(USERS_FILE))) {
                JSONObject usersRoot = new JSONObject();
                usersRoot.put("students", new JSONArray());
                usersRoot.put("instructors", new JSONArray());
                writeToFile(USERS_FILE, usersRoot.toString(4));
            }

            if (!Files.exists(Paths.get(COURSES_FILE))) {
                JSONObject coursesRoot = new JSONObject();
                coursesRoot.put("courses", new JSONArray());
                writeToFile(COURSES_FILE, coursesRoot.toString(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== USER OPERATIONS ==========

    // Save a new user (models.Student or models.Instructor)
    public boolean saveUser(User user) {
        try {
            String content = readFile(USERS_FILE);
            JSONObject root = new JSONObject(content);

            // Check for duplicate userId or email
            if (userExists(user.getUserId()) || emailExists(user.getEmail())) {
                return false;
            }

            JSONObject userJson = userToJson(user);

            if (user.getRole().equals("student")) {  // WAS: "models.Student"
                root.getJSONArray("students").put(userJson);
            } else if (user.getRole().equals("instructor")) {  // WAS: "models.Instructor"
                root.getJSONArray("instructors").put(userJson);
            }

            writeToFile(USERS_FILE, root.toString(4));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get user by userId
    public User getUserById(String userId) {
        try {
            String content = readFile(USERS_FILE);
            JSONObject root = new JSONObject(content);

            // Search in students
            JSONArray students = root.getJSONArray("students");
            for (int i = 0; i < students.length(); i++) {
                JSONObject userJson = students.getJSONObject(i);
                if (userJson.getString("userId").equals(userId)) {
                    return jsonToStudent(userJson);
                }
            }

            // Search in instructors
            JSONArray instructors = root.getJSONArray("instructors");
            for (int i = 0; i < instructors.length(); i++) {
                JSONObject userJson = instructors.getJSONObject(i);
                if (userJson.getString("userId").equals(userId)) {
                    return jsonToInstructor(userJson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get user by email (for login)
    public User getUserByEmail(String email) {
        try {
            String content = readFile(USERS_FILE);
            JSONObject root = new JSONObject(content);

            // Search in students
            JSONArray students = root.getJSONArray("students");
            for (int i = 0; i < students.length(); i++) {
                JSONObject userJson = students.getJSONObject(i);
                if (userJson.getString("email").equals(email)) {
                    return jsonToStudent(userJson);
                }
            }

            // Search in instructors
            JSONArray instructors = root.getJSONArray("instructors");
            for (int i = 0; i < instructors.length(); i++) {
                JSONObject userJson = instructors.getJSONObject(i);
                if (userJson.getString("email").equals(email)) {
                    return jsonToInstructor(userJson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update user data
    public boolean updateUser(User user) {
        try {
            String content = readFile(USERS_FILE);
            JSONObject root = new JSONObject(content);

            String arrayName = user.getRole().equals("student") ? "students" : "instructors";
            JSONArray users = root.getJSONArray(arrayName);

            for (int i = 0; i < users.length(); i++) {
                JSONObject userJson = users.getJSONObject(i);
                if (userJson.getString("userId").equals(user.getUserId())) {
                    users.put(i, userToJson(user));
                    writeToFile(USERS_FILE, root.toString(4));
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if user exists
    public boolean userExists(String userId) {
        return getUserById(userId) != null;
    }

    // Check if email exists
    public boolean emailExists(String email) {
        return getUserByEmail(email) != null;
    }

    // ========== COURSE OPERATIONS ==========

    // Save a new course
    public boolean saveCourse(Course course) {
        try {
            String content = readFile(COURSES_FILE);
            JSONObject root = new JSONObject(content);

            // Check for duplicate courseId
            if (courseExists(course.getCourseId())) {
                return false;
            }

            JSONArray courses = root.getJSONArray("courses");
            courses.put(courseToJson(course));

            writeToFile(COURSES_FILE, root.toString(4));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get course by ID
    public Course getCourseById(String courseId) {
        try {
            String content = readFile(COURSES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray courses = root.getJSONArray("courses");

            for (int i = 0; i < courses.length(); i++) {
                JSONObject courseJson = courses.getJSONObject(i);
                if (courseJson.getString("courseId").equals(courseId)) {
                    return jsonToCourse(courseJson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all courses
    public List<Course> getAllCourses() {
        List<Course> courseList = new ArrayList<>();
        try {
            String content = readFile(COURSES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray courses = root.getJSONArray("courses");

            for (int i = 0; i < courses.length(); i++) {
                courseList.add(jsonToCourse(courses.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }

    // Get courses by instructor ID
    public List<Course> getCoursesByInstructor(String instructorId) {
        List<Course> courseList = new ArrayList<>();
        try {
            String content = readFile(COURSES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray courses = root.getJSONArray("courses");

            for (int i = 0; i < courses.length(); i++) {
                JSONObject courseJson = courses.getJSONObject(i);
                if (courseJson.getString("instructorId").equals(instructorId)) {
                    courseList.add(jsonToCourse(courseJson));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }

    // Update course
    public boolean updateCourse(Course course) {
        try {
            String content = readFile(COURSES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray courses = root.getJSONArray("courses");

            for (int i = 0; i < courses.length(); i++) {
                JSONObject courseJson = courses.getJSONObject(i);
                if (courseJson.getString("courseId").equals(course.getCourseId())) {
                    courses.put(i, courseToJson(course));
                    writeToFile(COURSES_FILE, root.toString(4));
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete course
    public boolean deleteCourse(String courseId) {
        try {
            String content = readFile(COURSES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray courses = root.getJSONArray("courses");

            for (int i = 0; i < courses.length(); i++) {
                JSONObject courseJson = courses.getJSONObject(i);
                if (courseJson.getString("courseId").equals(courseId)) {
                    courses.remove(i);
                    writeToFile(COURSES_FILE, root.toString(4));
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if course exists
    public boolean courseExists(String courseId) {
        return getCourseById(courseId) != null;
    }

    // ========== LESSON OPERATIONS ==========

    // Get lessons by course ID
    public List<Lesson> getLessonsByCourse(String courseId) {
        Course course = getCourseById(courseId);
        if (course != null && course.getLessons() != null) {
            return Arrays.asList(course.getLessons());
        }
        return new ArrayList<>();
    }

    // Add lesson to course
    public boolean addLessonToCourse(String courseId, Lesson lesson) {
        Course course = getCourseById(courseId);
        if (course == null) return false;

        // Check for duplicate lessonId within the course
        if (course.getLessons() != null) {
            for (Lesson l : course.getLessons()) {
                if (l.getLessonId().equals(lesson.getLessonId())) {
                    return false; // Duplicate lesson ID
                }
            }
        }

        course.addLesson(lesson);
        return updateCourse(course);
    }

    // Update lesson in course
    public boolean updateLesson(String courseId, Lesson lesson) {
        Course course = getCourseById(courseId);
        if (course == null || course.getLessons() == null) return false;

        Lesson[] lessons = course.getLessons();
        for (int i = 0; i < lessons.length; i++) {
            if (lessons[i].getLessonId().equals(lesson.getLessonId())) {
                lessons[i] = lesson;
                course.setLessons(lessons);
                return updateCourse(course);
            }
        }
        return false;
    }

    // Delete lesson from course
    public boolean deleteLessonFromCourse(String courseId, String lessonId) {
        Course course = getCourseById(courseId);
        if (course == null || course.getLessons() == null) return false;

        List<Lesson> lessonList = new ArrayList<>(Arrays.asList(course.getLessons()));
        boolean removed = lessonList.removeIf(l -> l.getLessonId().equals(lessonId));

        if (removed) {
            course.setLessons(lessonList.toArray(new Lesson[0]));
            return updateCourse(course);
        }
        return false;
    }

    // ========== PROGRESS TRACKING ==========

    // Mark lesson as completed for a student
    public boolean markLessonCompleted(String studentId, String courseId, String lessonId) {
        User user = getUserById(studentId);
        if (!(user instanceof Student)) return false;

        Student student = (Student) user;

        // Get or create progress map
        Map<String, List<String>> progress = student.getProgress();
        if (progress == null) {
            progress = new HashMap<>();
        }

        // Get or create completed lessons list for this course
        List<String> completedLessons = progress.get(courseId);
        if (completedLessons == null) {
            completedLessons = new ArrayList<>();
        }

        // Add lesson if not already completed
        if (!completedLessons.contains(lessonId)) {
            completedLessons.add(lessonId);
            progress.put(courseId, completedLessons);
            student.setProgress(progress);
            return updateUser(student);
        }

        return true; // Already completed
    }

    // Get completed lessons for a student in a course
    public List<String> getCompletedLessons(String studentId, String courseId) {
        User user = getUserById(studentId);
        if (!(user instanceof Student)) return new ArrayList<>();

        Student student = (Student) user;
        Map<String, List<String>> progress = student.getProgress();

        if (progress != null && progress.containsKey(courseId)) {
            return progress.get(courseId);
        }

        return new ArrayList<>();
    }

    // ========== ENROLLMENT OPERATIONS ==========

    // Enroll student in course
    public boolean enrollStudent(String studentId, String courseId) {
        // Update student's enrolled courses
        User user = getUserById(studentId);
        if (!(user instanceof Student)) return false;

        Student student = (Student) user;
        List<String> enrolledCourses = student.getEnrolledCourses();
        if (enrolledCourses == null) {
            enrolledCourses = new ArrayList<>();
        }

        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
            student.setEnrolledCourses(enrolledCourses);

            // Update course's student list
            Course course = getCourseById(courseId);
            if (course != null) {
                List<String> students = course.getStudents();
                if (students == null) {
                    students = new ArrayList<>();
                }
                if (!students.contains(studentId)) {
                    students.add(studentId);
                    course.setStudents(students);
                    updateCourse(course);
                }
            }

            return updateUser(student);
        }

        return true; // Already enrolled
    }

    private JSONObject userToJson(User user) {
        JSONObject json = new JSONObject();
        json.put("userId", user.getUserId());
        json.put("role", user.getRole());
        json.put("username", user.getUsername());
        json.put("email", user.getEmail());
        json.put("passwordHash", user.getPasswordHash());

        if (user instanceof Student) {
            Student student = (Student) user;
            json.put("enrolledCourses", new JSONArray(
                    student.getEnrolledCourses() != null ? student.getEnrolledCourses() : new ArrayList<>()
            ));


            JSONObject progressJson = new JSONObject();
            if (student.getProgress() != null) {
                for (Map.Entry<String, List<String>> entry : student.getProgress().entrySet()) {
                    progressJson.put(entry.getKey(), new JSONArray(entry.getValue()));
                }
            }
            json.put("progress", progressJson);

        } else if (user instanceof Instructor) {
            Instructor instructor = (Instructor) user;
            json.put("createdCourses", new JSONArray(
                    instructor.getCreatedCourses() != null ? instructor.getCreatedCourses() : new ArrayList<>()
            ));
        }

        return json;
    }

    private Student jsonToStudent(JSONObject    json) {
        Student student = new Student();
        student.setUserId(json.getString("userId"));
        student.setRole(json.getString("role"));
        student.setUsername(json.getString("username"));
        student.setEmail(json.getString("email"));
        student.setPasswordHash(json.getString("passwordHash"));


        JSONArray enrolledArray = json.optJSONArray("enrolledCourses");
        if (enrolledArray != null) {
            List<String> enrolled = new ArrayList<>();
            for (int i = 0; i < enrolledArray.length(); i++) {
                enrolled.add(enrolledArray.getString(i));
            }
            student.setEnrolledCourses(enrolled);
        }

        // Parse progress
        JSONObject progressJson = json.optJSONObject("progress");
        if (progressJson != null) {
            Map<String, List<String>> progress = new HashMap<>();
            for (String courseId : progressJson.keySet()) {
                JSONArray lessonsArray = progressJson.getJSONArray(courseId);
                List<String> lessons = new ArrayList<>();
                for (int i = 0; i < lessonsArray.length(); i++) {
                    lessons.add(lessonsArray.getString(i));
                }
                progress.put(courseId, lessons);
            }
            student.setProgress(progress);
        }

        return student;
    }

    private Instructor jsonToInstructor(JSONObject json) {
        Instructor instructor = new Instructor();
        instructor.setUserId(json.getString("userId"));
        instructor.setRole(json.getString("role"));
        instructor.setUsername(json.getString("username"));
        instructor.setEmail(json.getString("email"));
        instructor.setPasswordHash(json.getString("passwordHash"));

        // Parse created courses
        JSONArray createdArray = json.optJSONArray("createdCourses");
        if (createdArray != null) {
            List<String> created = new ArrayList<>();
            for (int i = 0; i < createdArray.length(); i++) {
                created.add(createdArray.getString(i));
            }
            instructor.setCreatedCourses(created);
        }

        return instructor;
    }

    private JSONObject courseToJson(Course course) {
        JSONObject json = new JSONObject();
        json.put("courseId", course.getCourseId());
        json.put("title", course.getTitle());
        json.put("description", course.getDescription());
        json.put("instructorId", course.getInstructorId());

        // Convert lessons array
        JSONArray lessonsArray = new JSONArray();
        if (course.getLessons() != null) {
            for (Lesson lesson : course.getLessons()) {
                lessonsArray.put(lessonToJson(lesson));
            }
        }
        json.put("lessons", lessonsArray);

        // Convert students list
        json.put("students", new JSONArray(
                course.getStudents() != null ? course.getStudents() : new ArrayList<>()
        ));

        return json;
    }

    private Course jsonToCourse(JSONObject json) {
        Course course = new Course();
        course.setCourseId(json.getString("courseId"));
        course.setTitle(json.getString("title"));
        course.setDescription(json.getString("description"));
        course.setInstructorId(json.getString("instructorId"));

        // Parse lessons
        JSONArray lessonsArray = json.optJSONArray("lessons");
        if (lessonsArray != null) {
            List<Lesson> lessons = new ArrayList<>();
            for (int i = 0; i < lessonsArray.length(); i++) {
                lessons.add(jsonToLesson(lessonsArray.getJSONObject(i)));
            }
            course.setLessons(lessons.toArray(new Lesson[0]));
        }

        // Parse students
        JSONArray studentsArray = json.optJSONArray("students");
        if (studentsArray != null) {
            List<String> students = new ArrayList<>();
            for (int i = 0; i < studentsArray.length(); i++) {
                students.add(studentsArray.getString(i));
            }
            course.setStudents(students);
        }

        return course;
    }

    private JSONObject lessonToJson(Lesson lesson) {
        JSONObject json = new JSONObject();
        json.put("lessonId", lesson.getLessonId());
        json.put("title", lesson.getTitle());
        json.put("content", lesson.getContent());
        json.put("resources", new JSONArray(
                lesson.getResources() != null ? lesson.getResources() : new String[0]
        ));
        return json;
    }

    private Lesson jsonToLesson(JSONObject json) {
        Lesson lesson = new Lesson();
        lesson.setLessonId(json.getString("lessonId"));
        lesson.setTitle(json.getString("title"));
        lesson.setContent(json.getString("content"));

        JSONArray resourcesArray = json.optJSONArray("resources");
        if (resourcesArray != null) {
            String[] resources = new String[resourcesArray.length()];
            for (int i = 0; i < resourcesArray.length(); i++) {
                resources[i] = resourcesArray.getString(i);
            }
            lesson.setResources(resources);
        }

        return lesson;
    }

    // ========== FILE I/O HELPERS ==========

    private String readFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }

    private void writeToFile(String filename, String content) throws IOException {
        Files.write(Paths.get(filename), content.getBytes());
    }
}