package database;

import models.*;
import org.json.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JsonDatabaseManager {

    // ==================== CONSTANTS ====================
    private static final String USERS_FILE = "users.json";
    private static final String COURSES_FILE = "courses.json";
    private static final String QUIZZES_FILE = "quizzes.json";           // NEW
    private static final String CERTIFICATES_FILE = "certificates.json"; // NEW

    // Singleton pattern
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

    // ==================== INITIALIZE FILES (UPDATED) ====================

    private void initializeFiles() {
        try {
            // Initialize users.json
            if (!Files.exists(Paths.get(USERS_FILE))) {
                JSONObject usersRoot = new JSONObject();
                usersRoot.put("students", new JSONArray());
                usersRoot.put("instructors", new JSONArray());
                usersRoot.put("admins", new JSONArray()); // NEW
                writeToFile(USERS_FILE, usersRoot.toString(4));
            }

            // Initialize courses.json
            if (!Files.exists(Paths.get(COURSES_FILE))) {
                JSONObject coursesRoot = new JSONObject();
                coursesRoot.put("courses", new JSONArray());
                writeToFile(COURSES_FILE, coursesRoot.toString(4));
            }

            // Initialize quizzes.json (NEW)
            if (!Files.exists(Paths.get(QUIZZES_FILE))) {
                JSONObject quizzesRoot = new JSONObject();
                quizzesRoot.put("quizzes", new JSONArray());
                quizzesRoot.put("attempts", new JSONArray());
                writeToFile(QUIZZES_FILE, quizzesRoot.toString(4));
            }

            // Initialize certificates.json (NEW)
            if (!Files.exists(Paths.get(CERTIFICATES_FILE))) {
                JSONObject certificatesRoot = new JSONObject();
                certificatesRoot.put("certificates", new JSONArray());
                writeToFile(CERTIFICATES_FILE, certificatesRoot.toString(4));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // USER OPERATIONS (FROM LAB 7)

    public boolean saveUser(User user) {
        try {
            String content = readFile(USERS_FILE);
            JSONObject root = new JSONObject(content);

            if (userExists(user.getUserId()) || emailExists(user.getEmail())) {
                return false;
            }

            JSONObject userJson = userToJson(user);

            if (user.getRole().equals("student")) {
                root.getJSONArray("students").put(userJson);
            } else if (user.getRole().equals("instructor")) {
                root.getJSONArray("instructors").put(userJson);
            } else if (user.getRole().equals("admin")) { // NEW
                root.getJSONArray("admins").put(userJson);
            }

            writeToFile(USERS_FILE, root.toString(4));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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

            // Search in admins (NEW)
            JSONArray admins = root.optJSONArray("admins");
            if (admins != null) {
                for (int i = 0; i < admins.length(); i++) {
                    JSONObject userJson = admins.getJSONObject(i);
                    if (userJson.getString("userId").equals(userId)) {
                        return jsonToAdmin(userJson);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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

            // Search in admins (NEW)
            JSONArray admins = root.optJSONArray("admins");
            if (admins != null) {
                for (int i = 0; i < admins.length(); i++) {
                    JSONObject userJson = admins.getJSONObject(i);
                    if (userJson.getString("email").equals(email)) {
                        return jsonToAdmin(userJson);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(User user) {
        try {
            String content = readFile(USERS_FILE);
            JSONObject root = new JSONObject(content);

            String arrayName;
            if (user.getRole().equals("student")) {
                arrayName = "students";
            } else if (user.getRole().equals("instructor")) {
                arrayName = "instructors";
            } else if (user.getRole().equals("admin")) { // NEW
                arrayName = "admins";
            } else {
                return false;
            }

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

    public boolean userExists(String userId) {
        return getUserById(userId) != null;
    }

    public boolean emailExists(String email) {
        return getUserByEmail(email) != null;
    }

    // Get all users (NEW)
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try {
            String content = readFile(USERS_FILE);
            JSONObject root = new JSONObject(content);

            JSONArray students = root.getJSONArray("students");
            for (int i = 0; i < students.length(); i++) {
                userList.add(jsonToStudent(students.getJSONObject(i)));
            }

            JSONArray instructors = root.getJSONArray("instructors");
            for (int i = 0; i < instructors.length(); i++) {
                userList.add(jsonToInstructor(instructors.getJSONObject(i)));
            }

            JSONArray admins = root.optJSONArray("admins");
            if (admins != null) {
                for (int i = 0; i < admins.length(); i++) {
                    userList.add(jsonToAdmin(admins.getJSONObject(i)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    // COURSE OPERATIONS

    public boolean saveCourse(Course course) {
        try {
            String content = readFile(COURSES_FILE);
            JSONObject root = new JSONObject(content);

            if (courseExists(course.getCourseId())) {
                return false;
            }

            // Set default approval status for new courses (NEW)
            if (course.getApprovalStatus() == null) {
                course.setApprovalStatus("PENDING");
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

    public boolean courseExists(String courseId) {
        return getCourseById(courseId) != null;
    }

    // COURSE APPROVAL OPERATIONS (NEW)

    public List<Course> getPendingCourses() {
        return getCoursesByStatus("PENDING");
    }

    public List<Course> getApprovedCourses() {
        return getCoursesByStatus("APPROVED");
    }

    public List<Course> getRejectedCourses() {
        return getCoursesByStatus("REJECTED");
    }

    public List<Course> getCoursesByStatus(String status) {
        List<Course> courseList = new ArrayList<>();
        try {
            String content = readFile(COURSES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray courses = root.getJSONArray("courses");

            for (int i = 0; i < courses.length(); i++) {
                JSONObject courseJson = courses.getJSONObject(i);
                String courseStatus = courseJson.optString("approvalStatus", "PENDING");
                if (courseStatus.equals(status)) {
                    courseList.add(jsonToCourse(courseJson));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }

    public boolean updateCourseStatus(String courseId, String status) {
        try {
            String content = readFile(COURSES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray courses = root.getJSONArray("courses");

            for (int i = 0; i < courses.length(); i++) {
                JSONObject courseJson = courses.getJSONObject(i);
                if (courseJson.getString("courseId").equals(courseId)) {
                    courseJson.put("approvalStatus", status);
                    writeToFile(COURSES_FILE, root.toString(4));
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // LESSON OPERATIONS ( old )

    public List<Lesson> getLessonsByCourse(String courseId) {
        Course course = getCourseById(courseId);
        if (course != null && course.getLessons() != null) {
            return Arrays.asList(course.getLessons());
        }
        return new ArrayList<>();
    }

    public boolean addLessonToCourse(String courseId, Lesson lesson) {
        Course course = getCourseById(courseId);
        if (course == null) return false;

        if (course.getLessons() != null) {
            for (Lesson l : course.getLessons()) {
                if (l.getLessonId().equals(lesson.getLessonId())) {
                    return false;
                }
            }
        }

        course.addLesson(lesson);
        return updateCourse(course);
    }

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

    // PROGRESS TRACKING ( old )

    public boolean markLessonCompleted(String studentId, String courseId, String lessonId) {
        User user = getUserById(studentId);
        if (!(user instanceof Student)) return false;

        Student student = (Student) user;

        Map<String, List<String>> progress = student.getProgress();
        if (progress == null) {
            progress = new HashMap<>();
        }

        List<String> completedLessons = progress.get(courseId);
        if (completedLessons == null) {
            completedLessons = new ArrayList<>();
        }

        if (!completedLessons.contains(lessonId)) {
            completedLessons.add(lessonId);
            progress.put(courseId, completedLessons);
            student.setProgress(progress);
            return updateUser(student);
        }

        return true;
    }

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

    // ENROLLMENT OPERATIONS ( old )

    public boolean enrollStudent(String studentId, String courseId) {
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

        return true;
    }

    // QUIZ OPERATIONS

    public boolean saveQuiz(Quiz quiz) {
        try {
            String content = readFile(QUIZZES_FILE);
            JSONObject root = new JSONObject(content);

            if (quizExists(quiz.getQuizId())) {
                return false;
            }

            JSONArray quizzes = root.getJSONArray("quizzes");
            quizzes.put(quizToJson(quiz));

            writeToFile(QUIZZES_FILE, root.toString(4));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Quiz getQuizById(String quizId) {
        try {
            String content = readFile(QUIZZES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray quizzes = root.getJSONArray("quizzes");

            for (int i = 0; i < quizzes.length(); i++) {
                JSONObject quizJson = quizzes.getJSONObject(i);
                if (quizJson.getString("quizId").equals(quizId)) {
                    return jsonToQuiz(quizJson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Quiz getQuizByLessonId(String lessonId) {
        try {
            String content = readFile(QUIZZES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray quizzes = root.getJSONArray("quizzes");

            for (int i = 0; i < quizzes.length(); i++) {
                JSONObject quizJson = quizzes.getJSONObject(i);
                if (quizJson.getString("lessonId").equals(lessonId)) {
                    return jsonToQuiz(quizJson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizList = new ArrayList<>();
        try {
            String content = readFile(QUIZZES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray quizzes = root.getJSONArray("quizzes");

            for (int i = 0; i < quizzes.length(); i++) {
                quizList.add(jsonToQuiz(quizzes.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quizList;
    }

    public boolean updateQuiz(Quiz quiz) {
        try {
            String content = readFile(QUIZZES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray quizzes = root.getJSONArray("quizzes");

            for (int i = 0; i < quizzes.length(); i++) {
                JSONObject quizJson = quizzes.getJSONObject(i);
                if (quizJson.getString("quizId").equals(quiz.getQuizId())) {
                    quizzes.put(i, quizToJson(quiz));
                    writeToFile(QUIZZES_FILE, root.toString(4));
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteQuiz(String quizId) {
        try {
            String content = readFile(QUIZZES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray quizzes = root.getJSONArray("quizzes");

            for (int i = 0; i < quizzes.length(); i++) {
                JSONObject quizJson = quizzes.getJSONObject(i);
                if (quizJson.getString("quizId").equals(quizId)) {
                    quizzes.remove(i);
                    writeToFile(QUIZZES_FILE, root.toString(4));
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean quizExists(String quizId) {
        return getQuizById(quizId) != null;
    }

    // QUIZ ATTEMPT OPERATIONS

    public boolean saveQuizAttempt(QuizAttempt attempt) {
        try {
            String content = readFile(QUIZZES_FILE);
            JSONObject root = new JSONObject(content);

            JSONArray attempts = root.getJSONArray("attempts");
            attempts.put(quizAttemptToJson(attempt));

            writeToFile(QUIZZES_FILE, root.toString(4));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<QuizAttempt> getQuizAttempts(String studentId, String quizId) {
        List<QuizAttempt> attemptList = new ArrayList<>();
        try {
            String content = readFile(QUIZZES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray attempts = root.getJSONArray("attempts");

            for (int i = 0; i < attempts.length(); i++) {
                JSONObject attemptJson = attempts.getJSONObject(i);
                if (attemptJson.getString("studentId").equals(studentId) &&
                        attemptJson.getString("quizId").equals(quizId)) {
                    attemptList.add(jsonToQuizAttempt(attemptJson));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attemptList;
    }

    public List<QuizAttempt> getAllAttemptsForQuiz(String quizId) {
        List<QuizAttempt> attemptList = new ArrayList<>();
        try {
            String content = readFile(QUIZZES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray attempts = root.getJSONArray("attempts");

            for (int i = 0; i < attempts.length(); i++) {
                JSONObject attemptJson = attempts.getJSONObject(i);
                if (attemptJson.getString("quizId").equals(quizId)) {
                    attemptList.add(jsonToQuizAttempt(attemptJson));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attemptList;
    }

    public QuizAttempt getBestAttempt(String studentId, String quizId) {
        List<QuizAttempt> attempts = getQuizAttempts(studentId, quizId);

        if (attempts.isEmpty()) {
            return null;
        }

        QuizAttempt best = attempts.get(0);
        for (QuizAttempt attempt : attempts) {
            if (attempt.getPercentage() > best.getPercentage()) {
                best = attempt;
            }
        }

        return best;
    }

    public QuizAttempt getAttemptById(String attemptId) {
        try {
            String content = readFile(QUIZZES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray attempts = root.getJSONArray("attempts");

            for (int i = 0; i < attempts.length(); i++) {
                JSONObject attemptJson = attempts.getJSONObject(i);
                if (attemptJson.getString("attemptId").equals(attemptId)) {
                    return jsonToQuizAttempt(attemptJson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ============================================================
    //                CERTIFICATE OPERATIONS (NEW)
    // ============================================================

    public boolean saveCertificate(Certificate certificate) {
        try {
            String content = readFile(CERTIFICATES_FILE);
            JSONObject root = new JSONObject(content);

            if (certificateExists(certificate.getStudentId(), certificate.getCourseId())) {
                return false;
            }

            JSONArray certificates = root.getJSONArray("certificates");
            certificates.put(certificateToJson(certificate));

            writeToFile(CERTIFICATES_FILE, root.toString(4));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Certificate getCertificateById(String certificateId) {
        try {
            String content = readFile(CERTIFICATES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray certificates = root.getJSONArray("certificates");

            for (int i = 0; i < certificates.length(); i++) {
                JSONObject certJson = certificates.getJSONObject(i);
                if (certJson.getString("certificateId").equals(certificateId)) {
                    return jsonToCertificate(certJson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Certificate> getStudentCertificates(String studentId) {
        List<Certificate> certList = new ArrayList<>();
        try {
            String content = readFile(CERTIFICATES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray certificates = root.getJSONArray("certificates");

            for (int i = 0; i < certificates.length(); i++) {
                JSONObject certJson = certificates.getJSONObject(i);
                if (certJson.getString("studentId").equals(studentId)) {
                    certList.add(jsonToCertificate(certJson));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return certList;
    }

    public Certificate getCertificateForCourse(String studentId, String courseId) {
        try {
            String content = readFile(CERTIFICATES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray certificates = root.getJSONArray("certificates");

            for (int i = 0; i < certificates.length(); i++) {
                JSONObject certJson = certificates.getJSONObject(i);
                if (certJson.getString("studentId").equals(studentId) &&
                        certJson.getString("courseId").equals(courseId)) {
                    return jsonToCertificate(certJson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Certificate> getAllCertificates() {
        List<Certificate> certList = new ArrayList<>();
        try {
            String content = readFile(CERTIFICATES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray certificates = root.getJSONArray("certificates");

            for (int i = 0; i < certificates.length(); i++) {
                certList.add(jsonToCertificate(certificates.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return certList;
    }

    public boolean certificateExists(String studentId, String courseId) {
        return getCertificateForCourse(studentId, courseId) != null;
    }

    public boolean deleteCertificate(String certificateId) {
        try {
            String content = readFile(CERTIFICATES_FILE);
            JSONObject root = new JSONObject(content);
            JSONArray certificates = root.getJSONArray("certificates");

            for (int i = 0; i < certificates.length(); i++) {
                JSONObject certJson = certificates.getJSONObject(i);
                if (certJson.getString("certificateId").equals(certificateId)) {
                    certificates.remove(i);
                    writeToFile(CERTIFICATES_FILE, root.toString(4));
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

// user updates

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

            // NEW: Certificate IDs
            json.put("certificateIds", new JSONArray(
                    student.getCertificateIds() != null ? student.getCertificateIds() : new ArrayList<>()
            ));

        } else if (user instanceof Instructor) {
            Instructor instructor = (Instructor) user;
            json.put("createdCourses", new JSONArray(
                    instructor.getCreatedCourses() != null ? instructor.getCreatedCourses() : new ArrayList<>()
            ));
        }
        // Admin has no extra fields beyond base User

        return json;
    }

    private Student jsonToStudent(JSONObject json) {
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

        // NEW: Parse certificate IDs
        JSONArray certArray = json.optJSONArray("certificateIds");
        if (certArray != null) {
            List<String> certIds = new ArrayList<>();
            for (int i = 0; i < certArray.length(); i++) {
                certIds.add(certArray.getString(i));
            }
            student.setCertificateIds(certIds);
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

    // NEW: Admin conversion
    private Admin jsonToAdmin(JSONObject json) {
        Admin admin = new Admin();
        admin.setUserId(json.getString("userId"));
        admin.setRole(json.getString("role"));
        admin.setUsername(json.getString("username"));
        admin.setEmail(json.getString("email"));
        admin.setPasswordHash(json.getString("passwordHash"));
        return admin;
    }

    // course updates from lab 7

    private JSONObject courseToJson(Course course) {
        JSONObject json = new JSONObject();
        json.put("courseId", course.getCourseId());
        json.put("title", course.getTitle());
        json.put("description", course.getDescription());
        json.put("instructorId", course.getInstructorId());

        // NEW: Approval status
        json.put("approvalStatus", course.getApprovalStatus() != null ? course.getApprovalStatus() : "PENDING");

        JSONArray lessonsArray = new JSONArray();
        if (course.getLessons() != null) {
            for (Lesson lesson : course.getLessons()) {
                lessonsArray.put(lessonToJson(lesson));
            }
        }
        json.put("lessons", lessonsArray);

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

        // NEW: Parse approval status
        course.setApprovalStatus(json.optString("approvalStatus", "PENDING"));

        JSONArray lessonsArray = json.optJSONArray("lessons");
        if (lessonsArray != null) {
            List<Lesson> lessons = new ArrayList<>();
            for (int i = 0; i < lessonsArray.length(); i++) {
                lessons.add(jsonToLesson(lessonsArray.getJSONObject(i)));
            }
            course.setLessons(lessons.toArray(new Lesson[0]));
        }

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

    // lesson updates

    private JSONObject lessonToJson(Lesson lesson) {
        JSONObject json = new JSONObject();
        json.put("lessonId", lesson.getLessonId());
        json.put("title", lesson.getTitle());
        json.put("content", lesson.getContent());
        json.put("resources", new JSONArray(
                lesson.getResources() != null ? lesson.getResources() : new String[0]
        ));

        // NEW: Quiz fields
        json.put("quizId", lesson.getQuizId() != null ? lesson.getQuizId() : "");
        json.put("requiresQuizPass", lesson.isRequiresQuizPass());

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

        // NEW: Parse quiz fields
        String quizId = json.optString("quizId", "");
        lesson.setQuizId(quizId.isEmpty() ? null : quizId);
        lesson.setRequiresQuizPass(json.optBoolean("requiresQuizPass", false));

        return lesson;
    }

    // quiz methods ( new )

    private JSONObject quizToJson(Quiz quiz) {
        JSONObject json = new JSONObject();
        json.put("quizId", quiz.getQuizId());
        json.put("lessonId", quiz.getLessonId());
        json.put("title", quiz.getTitle());
        json.put("passingScore", quiz.getPassingScore());
        json.put("maxAttempts", quiz.getMaxAttempts());
        json.put("timeLimit", quiz.getTimeLimit());

        JSONArray questionsArray = new JSONArray();
        if (quiz.getQuestions() != null) {
            for (Question question : quiz.getQuestions()) {
                questionsArray.put(questionToJson(question));
            }
        }
        json.put("questions", questionsArray);

        return json;
    }

    private Quiz jsonToQuiz(JSONObject json) {
        Quiz quiz = new Quiz();
        quiz.setQuizId(json.getString("quizId"));
        quiz.setLessonId(json.getString("lessonId"));
        quiz.setTitle(json.getString("title"));
        quiz.setPassingScore(json.getInt("passingScore"));
        quiz.setMaxAttempts(json.getInt("maxAttempts"));
        quiz.setTimeLimit(json.getInt("timeLimit"));

        JSONArray questionsArray = json.optJSONArray("questions");
        if (questionsArray != null) {
            List<Question> questions = new ArrayList<>();
            for (int i = 0; i < questionsArray.length(); i++) {
                questions.add(jsonToQuestion(questionsArray.getJSONObject(i)));
            }
            quiz.setQuestions(questions);
        }

        return quiz;
    }

    // questions methods ( new )

    private JSONObject questionToJson(Question question) {
        JSONObject json = new JSONObject();
        json.put("questionId", question.getQuestionId());
        json.put("questionText", question.getQuestionText());
        json.put("correctOptionIndex", question.getCorrectOptionIndex());
        json.put("points", question.getPoints());

        JSONArray optionsArray = new JSONArray();
        if (question.getOptions() != null) {
            for (String option : question.getOptions()) {
                optionsArray.put(option != null ? option : "");
            }
        }
        json.put("options", optionsArray);

        return json;
    }

    private Question jsonToQuestion(JSONObject json) {
        Question question = new Question();
        question.setQuestionId(json.getString("questionId"));
        question.setQuestionText(json.getString("questionText"));
        question.setCorrectOptionIndex(json.getInt("correctOptionIndex"));
        question.setPoints(json.getInt("points"));

        JSONArray optionsArray = json.optJSONArray("options");
        if (optionsArray != null) {
            String[] options = new String[optionsArray.length()];
            for (int i = 0; i < optionsArray.length(); i++) {
                options[i] = optionsArray.getString(i);
            }
            question.setOptions(options);
        }

        return question;
    }

    // quiz ettempt methods

    private JSONObject quizAttemptToJson(QuizAttempt attempt) {
        JSONObject json = new JSONObject();
        json.put("attemptId", attempt.getAttemptId());
        json.put("quizId", attempt.getQuizId());
        json.put("studentId", attempt.getStudentId());
        json.put("courseId", attempt.getCourseId() != null ? attempt.getCourseId() : "");
        json.put("lessonId", attempt.getLessonId() != null ? attempt.getLessonId() : "");
        json.put("score", attempt.getScore());
        json.put("maxScore", attempt.getMaxScore());
        json.put("percentage", attempt.getPercentage());
        json.put("passed", attempt.isPassed());
        json.put("attemptDate", attempt.getAttemptDate());

        JSONObject answersJson = new JSONObject();
        if (attempt.getAnswers() != null) {
            for (Map.Entry<String, Integer> entry : attempt.getAnswers().entrySet()) {
                answersJson.put(entry.getKey(), entry.getValue());
            }
        }
        json.put("answers", answersJson);

        return json;
    }

    private QuizAttempt jsonToQuizAttempt(JSONObject json) {
        QuizAttempt attempt = new QuizAttempt();
        attempt.setAttemptId(json.getString("attemptId"));
        attempt.setQuizId(json.getString("quizId"));
        attempt.setStudentId(json.getString("studentId"));
        attempt.setCourseId(json.optString("courseId", ""));
        attempt.setLessonId(json.optString("lessonId", ""));
        attempt.setScore(json.getInt("score"));
        attempt.setMaxScore(json.getInt("maxScore"));
        attempt.setPercentage(json.getDouble("percentage"));
        attempt.setPassed(json.getBoolean("passed"));
        attempt.setAttemptDate(json.getString("attemptDate"));

        JSONObject answersJson = json.optJSONObject("answers");
        if (answersJson != null) {
            Map<String, Integer> answers = new HashMap<>();
            for (String key : answersJson.keySet()) {
                answers.put(key, answersJson.getInt(key));
            }
            attempt.setAnswers(answers);
        }

        return attempt;
    }

    // certificates

    private JSONObject certificateToJson(Certificate certificate) {
        JSONObject json = new JSONObject();
        json.put("certificateId", certificate.getCertificateId());
        json.put("studentId", certificate.getStudentId());
        json.put("courseId", certificate.getCourseId());
        json.put("studentName", certificate.getStudentName() != null ? certificate.getStudentName() : "");
        json.put("courseName", certificate.getCourseName() != null ? certificate.getCourseName() : "");
        json.put("instructorName", certificate.getInstructorName() != null ? certificate.getInstructorName() : "");
        json.put("issueDate", certificate.getIssueDate());
        json.put("finalScore", certificate.getFinalScore());
        json.put("totalLessons", certificate.getTotalLessons());
        json.put("completedLessons", certificate.getCompletedLessons());

        return json;
    }

    private Certificate jsonToCertificate(JSONObject json) {
        Certificate certificate = new Certificate();
        certificate.setCertificateId(json.getString("certificateId"));
        certificate.setStudentId(json.getString("studentId"));
        certificate.setCourseId(json.getString("courseId"));
        certificate.setStudentName(json.optString("studentName", ""));
        certificate.setCourseName(json.optString("courseName", ""));
        certificate.setInstructorName(json.optString("instructorName", ""));
        certificate.setIssueDate(json.getString("issueDate"));
        certificate.setFinalScore(json.getDouble("finalScore"));
        certificate.setTotalLessons(json.optInt("totalLessons", 0));
        certificate.setCompletedLessons(json.optInt("completedLessons", 0));

        return certificate;
    }

    // file handeling

    private String readFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }

    private void writeToFile(String filename, String content) throws IOException {
        Files.write(Paths.get(filename), content.getBytes());
    }
}

//package database;
//
//public class JsonDatabaseManager {
//
//    // Singleton
//    private static JsonDatabaseManager instance;
//
//    private final UserDatabase users;
//    private final CourseDatabase courses;
//    private final QuizDatabase quizzes;
//
//    private JsonDatabaseManager() {
//        users = new UserDatabase();
//        courses = new CourseDatabase();
//        quizzes = new QuizDatabase();
//    }
//
//    public static JsonDatabaseManager getInstance() {
//        if (instance == null)
//            instance = new JsonDatabaseManager();
//
//        return instance;
//    }
//
//    // Accessors
//    public UserDatabase users() { return users; }
//    public CourseDatabase courses() { return courses; }
//    public QuizDatabase quizzes() { return quizzes; }
//}
