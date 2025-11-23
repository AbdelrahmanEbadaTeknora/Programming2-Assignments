package database;

import models.*;
import org.json.*;
import java.nio.file.*;
import java.util.*;

public class CourseDatabase {

    private static final String COURSES_FILE = "courses.json";

    public CourseDatabase() {
        initialize();
    }

    private void initialize() {
        try {
            if (!Files.exists(Paths.get(COURSES_FILE))) {
                JSONObject root = new JSONObject();
                root.put("courses", new JSONArray());
                Files.write(Paths.get(COURSES_FILE), root.toString(4).getBytes());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // =====================================================
    // COURSE CRUD
    // =====================================================

    public boolean saveCourse(Course course) {
        try {
            JSONObject root = read();

            if (courseExists(course.getCourseId())) return false;

            if (course.getApprovalStatus() == null)
                course.setApprovalStatus("PENDING");

            root.getJSONArray("courses").put(courseToJson(course));
            write(root);
            return true;

        } catch (Exception e) { return false; }
    }

    public Course getCourseById(String id) {
        try {
            JSONObject root = read();
            JSONArray courses = root.getJSONArray("courses");

            for (int i = 0; i < courses.length(); i++) {
                JSONObject c = courses.getJSONObject(i);
                if (c.getString("courseId").equals(id))
                    return jsonToCourse(c);
            }
        } catch (Exception ignored) {}
        return null;
    }

    public boolean updateCourse(Course course) {
        try {
            JSONObject root = read();
            JSONArray arr = root.getJSONArray("courses");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject c = arr.getJSONObject(i);
                if (c.getString("courseId").equals(course.getCourseId())) {
                    arr.put(i, courseToJson(course));
                    write(root);
                    return true;
                }
            }

        } catch (Exception ignored) {}
        return false;
    }

    public boolean deleteCourse(String courseId) {
        try {
            JSONObject root = read();
            JSONArray arr = root.getJSONArray("courses");

            for (int i = 0; i < arr.length(); i++) {
                if (arr.getJSONObject(i).getString("courseId").equals(courseId)) {
                    arr.remove(i);
                    write(root);
                    return true;
                }
            }

        } catch (Exception ignored) {}
        return false;
    }

    public boolean courseExists(String id) {
        return getCourseById(id) != null;
    }

    // =====================================================
    // COURSE APPROVAL
    // =====================================================

    public boolean updateCourseStatus(String courseId, String status) {
        try {
            JSONObject root = read();
            JSONArray arr = root.getJSONArray("courses");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject c = arr.getJSONObject(i);

                if (c.getString("courseId").equals(courseId)) {
                    c.put("approvalStatus", status);
                    write(root);
                    return true;
                }
            }

        } catch (Exception ignored) {}
        return false;
    }

    public List<Course> getCoursesByStatus(String status) {
        List<Course> list = new ArrayList<>();
        try {
            JSONObject root = read();
            JSONArray arr = root.getJSONArray("courses");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject c = arr.getJSONObject(i);
                if (c.optString("approvalStatus", "PENDING").equals(status)) {
                    list.add(jsonToCourse(c));
                }
            }
        } catch (Exception ignored) {}

        return list;
    }

    public List<Course> getPendingCourses() { return getCoursesByStatus("PENDING"); }
    public List<Course> getApprovedCourses() { return getCoursesByStatus("APPROVED"); }
    public List<Course> getRejectedCourses() { return getCoursesByStatus("REJECTED"); }

    // =====================================================
    // LESSON OPERATIONS
    // =====================================================

    public List<Lesson> getLessons(String courseId) {
        Course c = getCourseById(courseId);
        if (c != null && c.getLessons() != null)
            return Arrays.asList(c.getLessons());

        return new ArrayList<>();
    }

    public boolean addLesson(String courseId, Lesson lesson) {
        Course c = getCourseById(courseId);
        if (c == null) return false;

        if (c.getLessons() != null) {
            for (Lesson l : c.getLessons())
                if (l.getLessonId().equals(lesson.getLessonId()))
                    return false;
        }

        c.addLesson(lesson);
        return updateCourse(c);
    }

    public boolean updateLesson(String courseId, Lesson lesson) {
        Course c = getCourseById(courseId);
        if (c == null || c.getLessons() == null) return false;

        Lesson[] lessons = c.getLessons();

        for (int i = 0; i < lessons.length; i++) {
            if (lessons[i].getLessonId().equals(lesson.getLessonId())) {
                lessons[i] = lesson;
                c.setLessons(lessons);
                return updateCourse(c);
            }
        }
        return false;
    }

    public boolean deleteLesson(String courseId, String lessonId) {
        Course c = getCourseById(courseId);
        if (c == null || c.getLessons() == null) return false;

        List<Lesson> arr = new ArrayList<>(Arrays.asList(c.getLessons()));
        boolean removed = arr.removeIf(l -> l.getLessonId().equals(lessonId));

        if (removed) {
            c.setLessons(arr.toArray(new Lesson[0]));
            return updateCourse(c);
        }
        return false;
    }

    // =====================================================
    // JSON HELPERS
    // =====================================================

    private JSONObject read() throws Exception {
        return new JSONObject(new String(Files.readAllBytes(Paths.get(COURSES_FILE))));
    }

    private void write(JSONObject obj) throws Exception {
        Files.write(Paths.get(COURSES_FILE), obj.toString(4).getBytes());
    }

    private JSONObject courseToJson(Course c) {
        JSONObject j = new JSONObject();

        j.put("courseId", c.getCourseId());
        j.put("title", c.getTitle());
        j.put("description", c.getDescription());
        j.put("instructorId", c.getInstructorId());
        j.put("approvalStatus", c.getApprovalStatus());

        JSONArray les = new JSONArray();
        if (c.getLessons() != null)
            for (Lesson l : c.getLessons())
                les.put(lessonToJson(l));

        j.put("lessons", les);

        j.put("students", new JSONArray(c.getStudents() != null ? c.getStudents() : new ArrayList<>()));
        return j;
    }

    private Course jsonToCourse(JSONObject j) {
        Course c = new Course();

        c.setCourseId(j.getString("courseId"));
        c.setTitle(j.getString("title"));
        c.setDescription(j.getString("description"));
        c.setInstructorId(j.getString("instructorId"));
        c.setApprovalStatus(j.optString("approvalStatus", "PENDING"));

        List<Lesson> lessons = new ArrayList<>();
        JSONArray arr = j.optJSONArray("lessons");
        if (arr != null)
            for (Object o : arr)
                lessons.add(jsonToLesson((JSONObject)o));

        c.setLessons(lessons.toArray(new Lesson[0]));

        List<String> std = new ArrayList<>();
        JSONArray stArr = j.optJSONArray("students");
        if (stArr != null)
            for (Object o : stArr) std.add((String)o);

        c.setStudents(std);

        return c;
    }

    private JSONObject lessonToJson(Lesson l) {
        JSONObject j = new JSONObject();
        j.put("lessonId", l.getLessonId());
        j.put("title", l.getTitle());
        j.put("content", l.getContent());

        j.put("resources", new JSONArray(l.getResources() != null ? l.getResources() : new String[0]));

        j.put("quizId", l.getQuizId() != null ? l.getQuizId() : "");
        j.put("requiresQuizPass", l.isRequiresQuizPass());

        return j;
    }

    private Lesson jsonToLesson(JSONObject j) {
        Lesson l = new Lesson();

        l.setLessonId(j.getString("lessonId"));
        l.setTitle(j.getString("title"));
        l.setContent(j.getString("content"));

        JSONArray arr = j.optJSONArray("resources");
        if (arr != null) {
            String[] res = new String[arr.length()];
            for (int i = 0; i < arr.length(); i++) res[i] = arr.getString(i);
            l.setResources(res);
        }

        String qid = j.optString("quizId", "");
        l.setQuizId(qid.isEmpty() ? null : qid);

        l.setRequiresQuizPass(j.optBoolean("requiresQuizPass", false));

        return l;
    }
}
