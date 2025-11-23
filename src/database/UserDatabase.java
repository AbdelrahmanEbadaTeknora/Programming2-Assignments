package database;

import models.*;
import org.json.*;
import java.nio.file.*;
import java.util.*;

public class UserDatabase {

    private static final String USERS_FILE = "users.json";

    public UserDatabase() {
        initialize();
    }

    private void initialize() {
        try {
            if (!Files.exists(Paths.get(USERS_FILE))) {
                JSONObject root = new JSONObject();
                root.put("students", new JSONArray());
                root.put("instructors", new JSONArray());
                root.put("admins", new JSONArray());
                Files.write(Paths.get(USERS_FILE), root.toString(4).getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ====================================
    // USER SAVE / GET / UPDATE
    // ====================================

    public boolean saveUser(User user) {
        try {
            JSONObject root = read();
            if (userExists(user.getUserId()) || emailExists(user.getEmail())) return false;

            JSONObject obj = userToJson(user);

            switch (user.getRole()) {
                case "student": root.getJSONArray("students").put(obj); break;
                case "instructor": root.getJSONArray("instructors").put(obj); break;
                case "admin": root.getJSONArray("admins").put(obj); break;
            }

            write(root);
            return true;
        } catch (Exception e) { return false; }
    }

    public User getUserById(String id) {
        try {
            JSONObject root = read();

            JSONArray students = root.getJSONArray("students");
            for (int i = 0; i < students.length(); i++)
                if (students.getJSONObject(i).getString("userId").equals(id))
                    return jsonToStudent(students.getJSONObject(i));

            JSONArray instructors = root.getJSONArray("instructors");
            for (int i = 0; i < instructors.length(); i++)
                if (instructors.getJSONObject(i).getString("userId").equals(id))
                    return jsonToInstructor(instructors.getJSONObject(i));

            JSONArray admins = root.getJSONArray("admins");
            for (int i = 0; i < admins.length(); i++)
                if (admins.getJSONObject(i).getString("userId").equals(id))
                    return jsonToAdmin(admins.getJSONObject(i));

        } catch (Exception ignored) {}

        return null;
    }

    public boolean updateUser(User user) {
        try {
            JSONObject root = read();

            String arrName = user.getRole() + "s"; // students / instructors / admins
            JSONArray arr = root.getJSONArray(arrName);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                if (o.getString("userId").equals(user.getUserId())) {
                    arr.put(i, userToJson(user));
                    write(root);
                    return true;
                }
            }

        } catch (Exception ignored) {}
        return false;
    }

    public boolean userExists(String id) { return getUserById(id) != null; }

    public boolean emailExists(String email) {
        return getUserByEmail(email) != null;
    }

    public User getUserByEmail(String email) {
        try {
            JSONObject root = read();

            for (Object o : root.getJSONArray("students"))
                if (((JSONObject)o).getString("email").equals(email))
                    return jsonToStudent((JSONObject)o);

            for (Object o : root.getJSONArray("instructors"))
                if (((JSONObject)o).getString("email").equals(email))
                    return jsonToInstructor((JSONObject)o);

            for (Object o : root.getJSONArray("admins"))
                if (((JSONObject)o).getString("email").equals(email))
                    return jsonToAdmin((JSONObject)o);

        } catch (Exception ignored) {}
        return null;
    }

    // ====================================
    // ENROLLMENT
    // ====================================

    public boolean enrollStudent(String studentId, String courseId) {
        User u = getUserById(studentId);
        if (!(u instanceof Student)) return false;

        Student s = (Student) u;
        if (!s.getEnrolledCourses().contains(courseId)) {
            s.getEnrolledCourses().add(courseId);
            return updateUser(s);
        }
        return true;
    }

    // ====================================
    // LESSON COMPLETION
    // ====================================

    public boolean markLessonCompleted(String studentId, String courseId, String lessonId) {
        User u = getUserById(studentId);
        if (!(u instanceof Student)) return false;

        Student s = (Student) u;
        Map<String,List<String>> progress = s.getProgress();

        progress.putIfAbsent(courseId, new ArrayList<>());
        if (!progress.get(courseId).contains(lessonId)) {
            progress.get(courseId).add(lessonId);
            return updateUser(s);
        }
        return true;
    }

    // ====================================
    // JSON HELPERS
    // ====================================

    private JSONObject read() throws Exception {
        return new JSONObject(new String(Files.readAllBytes(Paths.get(USERS_FILE))));
    }

    private void write(JSONObject obj) throws Exception {
        Files.write(Paths.get(USERS_FILE), obj.toString(4).getBytes());
    }

    private JSONObject userToJson(User u) {
        JSONObject j = new JSONObject();
        j.put("userId", u.getUserId());
        j.put("role", u.getRole());
        j.put("username", u.getUsername());
        j.put("email", u.getEmail());
        j.put("passwordHash", u.getPasswordHash());

        if (u instanceof Student) {
            Student s = (Student) u;
            j.put("enrolledCourses", new JSONArray(s.getEnrolledCourses()));
            j.put("certificateIds", new JSONArray(s.getCertificateIds()));

            JSONObject prog = new JSONObject();
            for (String cid : s.getProgress().keySet())
                prog.put(cid, new JSONArray(s.getProgress().get(cid)));

            j.put("progress", prog);
        }

        if (u instanceof Instructor) {
            Instructor inst = (Instructor) u;
            j.put("createdCourses", new JSONArray(inst.getCreatedCourses()));
        }

        return j;
    }

    private Student jsonToStudent(JSONObject j) {
        Student s = new Student();
        s.setUserId(j.getString("userId"));
        s.setRole("student");
        s.setUsername(j.getString("username"));
        s.setEmail(j.getString("email"));
        s.setPasswordHash(j.getString("passwordHash"));

        List<String> enrolled = new ArrayList<>();
        JSONArray arr = j.optJSONArray("enrolledCourses");
        if (arr != null) for (Object o : arr) enrolled.add((String) o);
        s.setEnrolledCourses(enrolled);

        Map<String,List<String>> prog = new HashMap<>();
        JSONObject pJson = j.optJSONObject("progress");
        if (pJson != null) {
            for (String cid : pJson.keySet()) {
                List<String> done = new ArrayList<>();
                JSONArray la = pJson.getJSONArray(cid);
                for (Object o : la) done.add((String)o);
                prog.put(cid, done);
            }
        }
        s.setProgress(prog);

        List<String> certIds = new ArrayList<>();
        JSONArray cs = j.optJSONArray("certificateIds");
        if (cs != null) for (Object o : cs) certIds.add((String)o);
        s.setCertificateIds(certIds);

        return s;
    }

    private Instructor jsonToInstructor(JSONObject j) {
        Instructor i = new Instructor();
        i.setUserId(j.getString("userId"));
        i.setUsername(j.getString("username"));
        i.setEmail(j.getString("email"));
        i.setRole("instructor");
        i.setPasswordHash(j.getString("passwordHash"));

        List<String> created = new ArrayList<>();
        JSONArray arr = j.optJSONArray("createdCourses");
        if (arr != null) for (Object o : arr) created.add((String)o);
        i.setCreatedCourses(created);

        return i;
    }

    private Admin jsonToAdmin(JSONObject j) {
        Admin a = new Admin();
        a.setUserId(j.getString("userId"));
        a.setRole("admin");
        a.setUsername(j.getString("username"));
        a.setEmail(j.getString("email"));
        a.setPasswordHash(j.getString("passwordHash"));
        return a;
    }
}
