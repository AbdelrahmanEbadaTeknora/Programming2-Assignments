//package database;
//
//import models.*;
//import org.json.*;
//import java.nio.file.*;
//import java.util.*;
//
//public class QuizDatabase {
//
//    private static final String QUIZZES_FILE = "quizzes.json";
//    private static final String CERTIFICATES_FILE = "certificates.json";
//
//    public QuizDatabase() {
//        initialize();
//    }
//
//    // =====================================================
//    // INITIALIZATION
//    // =====================================================
//
//    private void initialize() {
//        try {
//            if (!Files.exists(Paths.get(QUIZZES_FILE))) {
//                JSONObject root = new JSONObject();
//                root.put("quizzes", new JSONArray());
//                root.put("attempts", new JSONArray());
//                Files.write(Paths.get(QUIZZES_FILE), root.toString(4).getBytes());
//            }
//
//            if (!Files.exists(Paths.get(CERTIFICATES_FILE))) {
//                JSONObject root = new JSONObject();
//                root.put("certificates", new JSONArray());
//                Files.write(Paths.get(CERTIFICATES_FILE), root.toString(4).getBytes());
//            }
//
//        } catch (Exception e) { e.printStackTrace(); }
//    }
//
//    // =====================================================
//    // QUIZ CRUD
//    // =====================================================
//
//    public boolean saveQuiz(Quiz q) {
//        try {
//            JSONObject root = read(QUIZZES_FILE);
//
//            if (quizExists(q.getQuizId())) return false;
//
//            root.getJSONArray("quizzes").put(quizToJson(q));
//            write(QUIZZES_FILE, root);
//            return true;
//
//        } catch (Exception e) { return false; }
//    }
//
//    public Quiz getQuizById(String id) {
//        try {
//            JSONObject root = read(QUIZZES_FILE);
//            JSONArray arr = root.getJSONArray("quizzes");
//
//            for (int i = 0; i < arr.length(); i++) {
//                JSONObject q = arr.getJSONObject(i);
//                if (q.getString("quizId").equals(id))
//                    return jsonToQuiz(q);
//            }
//        } catch (Exception ignored) {}
//
//        return null;
//    }
//
//    public Quiz getQuizByLesson(String lessonId) {
//        try {
//            JSONObject root = read(QUIZZES_FILE);
//            JSONArray arr = root.getJSONArray("quizzes");
//
//            for (int i = 0; i < arr.length(); i++) {
//                JSONObject q = arr.getJSONObject(i);
//                if (q.getString("lessonId").equals(lessonId))
//                    return jsonToQuiz(q);
//            }
//        } catch (Exception ignored) {}
//
//        return null;
//    }
//
//    public boolean updateQuiz(Quiz quiz) {
//        try {
//            JSONObject root = read(QUIZZES_FILE);
//            JSONArray arr = root.getJSONArray("quizzes");
//
//            for (int i = 0; i < arr.length(); i++) {
//                if (arr.getJSONObject(i).getString("quizId").equals(quiz.getQuizId())) {
//                    arr.put(i, quizToJson(quiz));
//                    write(QUIZZES_FILE, root);
//                    return true;
//                }
//            }
//
//        } catch (Exception ignored) {}
//        return false;
//    }
//
//    public boolean deleteQuiz(String quizId) {
//        try {
//            JSONObject root = read(QUIZZES_FILE);
//            JSONArray arr = root.getJSONArray("quizzes");
//
//            for (int i = 0; i < arr.length(); i++) {
//                if (arr.getJSONObject(i).getString("quizId").equals(quizId)) {
//                    arr.remove(i);
//                    write(QUIZZES_FILE, root);
//                    return true;
//                }
//            }
//
//        } catch (Exception ignored) {}
//        return false;
//    }
//
//    public boolean quizExists(String id) {
//        return getQuizById(id) != null;
//    }
//
//    // =====================================================
//    // QUIZ ATTEMPTS
//    // =====================================================
//
//    public boolean saveAttempt(QuizAttempt attempt) {
//        try {
//            JSONObject root = read(QUIZZES_FILE);
//            root.getJSONArray("attempts").put(quizAttemptToJson(attempt));
//            write(QUIZZES_FILE, root);
//            return true;
//        } catch (Exception e) { return false; }
//    }
//
//    public List<QuizAttempt> getAttempts(String studentId, String quizId) {
//        List<QuizAttempt> list = new ArrayList<>();
//
//        try {
//            JSONObject root = read(QUIZZES_FILE);
//            JSONArray arr = root.getJSONArray("attempts");
//
//            for (int i = 0; i < arr.length(); i++) {
//                JSONObject a = arr.getJSONObject(i);
//
//                if (a.getString("studentId").equals(studentId) &&
//                        a.getString("quizId").equals(quizId))
//                    list.add(jsonToAttempt(a));
//            }
//
//        } catch (Exception ignored) {}
//
//        return list;
//    }
//
//    public QuizAttempt getBestAttempt(String studentId, String quizId) {
//        List<QuizAttempt> attempts = getAttempts(studentId, quizId);
//        if (attempts.isEmpty()) return null;
//
//        QuizAttempt best = attempts.get(0);
//        for (QuizAttempt a : attempts)
//            if (a.getPercentage() > best.getPercentage())
//                best = a;
//
//        return best;
//    }
//
//    public QuizAttempt getAttemptById(String attemptId) {
//        try {
//            JSONObject root = read(QUIZZES_FILE);
//            JSONArray arr = root.getJSONArray("attempts");
//
//            for (int i = 0; i < arr.length(); i++) {
//                JSONObject a = arr.getJSONObject(i);
//                if (a.getString("attemptId").equals(attemptId))
//                    return jsonToAttempt(a);
//            }
//
//        } catch (Exception ignored) {}
//
//        return null;
//    }
//
//    // =====================================================
//    // CERTIFICATES
//    // =====================================================
//
//    public boolean saveCertificate(Certificate cert) {
//        try {
//            JSONObject root = read(CERTIFICATES_FILE);
//
//            if (certificateExists(cert.getStudentId(), cert.getCourseId()))
//                return false;
//
//            root.getJSONArray("certificates").put(certificateToJson(cert));
//            write(CERTIFICATES_FILE, root);
//
//            return true;
//
//        } catch (Exception e) { return false; }
//    }
//
//    public boolean certificateExists(String studentId, String courseId) {
//        return getCertificate(studentId, courseId) != null;
//    }
//
//    public Certificate getCertificate(String studentId, String courseId) {
//        try {
//            JSONObject root = read(CERTIFICATES_FILE);
//            JSONArray arr = root.getJSONArray("certificates");
//
//            for (int i = 0; i < arr.length(); i++) {
//                JSONObject c = arr.getJSONObject(i);
//
//                if (c.getString("studentId").equals(studentId) &&
//                        c.getString("courseId").equals(courseId))
//                    return jsonToCertificate(c);
//            }
//
//        } catch (Exception ignored) {}
//        return null;
//    }
//
//    public Certificate getCertificateById(String id) {
//        try {
//            JSONObject root = read(CERTIFICATES_FILE);
//            JSONArray arr = root.getJSONArray("certificates");
//
//            for (int i = 0; i < arr.length(); i++) {
//                JSONObject c = arr.getJSONObject(i);
//
//                if (c.getString("certificateId").equals(id))
//                    return jsonToCertificate(c);
//            }
//
//        } catch (Exception ignored) {}
//        return null;
//    }
//
//    public List<Certificate> getAllCertificates() {
//        List<Certificate> list = new ArrayList<>();
//
//        try {
//            JSONObject root = read(CERTIFICATES_FILE);
//            JSONArray arr = root.getJSONArray("certificates");
//
//            for (int i = 0; i < arr.length(); i++)
//                list.add(jsonToCertificate(arr.getJSONObject(i)));
//
//        } catch (Exception ignored) {}
//
//        return list;
//    }
//
//    public boolean deleteCertificate(String certificateId) {
//        try {
//            JSONObject root = read(CERTIFICATES_FILE);
//            JSONArray arr = root.getJSONArray("certificates");
//
//            for (int i = 0; i < arr.length(); i++) {
//                if (arr.getJSONObject(i).getString("certificateId").equals(certificateId)) {
//                    arr.remove(i);
//                    write(CERTIFICATES_FILE, root);
//                    return true;
//                }
//            }
//
//        } catch (Exception ignored) {}
//        return false;
//    }
//
//    // =====================================================
//    // JSON HELPERS
//    // =====================================================
//
//    private JSONObject read(String file) throws Exception {
//        return new JSONObject(new String(Files.readAllBytes(Paths.get(file))));
//    }
//
//    private void write(String file, JSONObject obj) throws Exception {
//        Files.write(Paths.get(file), obj.toString(4).getBytes());
//    }
//
//    // ---------- QUIZ JSON ----------
//
//    private JSONObject quizToJson(Quiz q) {
//        JSONObject j = new JSONObject();
//
//        j.put("quizId", q.getQuizId());
//        j.put("lessonId", q.getLessonId());
//        j.put("title", q.getTitle());
//        j.put("passingScore", q.getPassingScore());
//        j.put("maxAttempts", q.getMaxAttempts());
//        j.put("timeLimit", q.getTimeLimit());
//
//        JSONArray questions = new JSONArray();
//        if (q.getQuestions() != null)
//            for (Question qs : q.getQuestions())
//                questions.put(questionToJson(qs));
//
//        j.put("questions", questions);
//        return j;
//    }
//
//    private Quiz jsonToQuiz(JSONObject j) {
//        Quiz q = new Quiz();
//
//        q.setQuizId(j.getString("quizId"));
//        q.setLessonId(j.getString("lessonId"));
//        q.setTitle(j.getString("title"));
//        q.setPassingScore(j.getInt("passingScore"));
//        q.setMaxAttempts(j.getInt("maxAttempts"));
//        q.setTimeLimit(j.getInt("timeLimit"));
//
//        List<Question> list = new ArrayList<>();
//        JSONArray arr = j.optJSONArray("questions");
//        if (arr != null)
//            for (Object o : arr)
//                list.add(jsonToQuestion((JSONObject)o));
//
//        q.setQuestions(list);
//        return q;
//    }
//
//    // ---------- QUESTION JSON ----------
//
//    private JSONObject questionToJson(Question q) {
//        JSONObject j = new JSONObject();
//
//        j.put("questionId", q.getQuestionId());
//        j.put("questionText", q.getQuestionText());
//        j.put("correctOptionIndex", q.getCorrectOptionIndex());
//        j.put("points", q.getPoints());
//
//        JSONArray opt = new JSONArray(q.getOptions() != null ? q.getOptions() : new String[0]);
//        j.put("options", opt);
//
//        return j;
//    }
//
//    private Question jsonToQuestion(JSONObject j) {
//        Question q = new Question();
//
//        q.setQuestionId(j.getString("questionId"));
//        q.setQuestionText(j.getString("questionText"));
//        q.setCorrectOptionIndex(j.getInt("correctOptionIndex"));
//        q.setPoints(j.getInt("points"));
//
//        JSONArray arr = j.optJSONArray("options");
//        if (arr != null) {
//            String[] ops = new String[arr.length()];
//            for (int i = 0; i < arr.length(); i++) ops[i] = arr.getString(i);
//            q.setOptions(ops);
//        }
//
//        return q;
//    }
//
//    // ---------- ATTEMPT JSON ----------
//
//    private JSONObject quizAttemptToJson(QuizAttempt a) {
//        JSONObject j = new JSONObject();
//
//        j.put("attemptId", a.getAttemptId());
//        j.put("quizId", a.getQuizId());
//        j.put("studentId", a.getStudentId());
//        j.put("courseId", a.getCourseId());
//        j.put("lessonId", a.getLessonId());
//        j.put("score", a.getScore());
//        j.put("maxScore", a.getMaxScore());
//        j.put("percentage", a.getPercentage());
//        j.put("passed", a.isPassed());
//        j.put("attemptDate", a.getAttemptDate());
//
//        JSONObject ans = new JSONObject();
//        if (a.getAnswers() != null)
//            for (String key : a.getAnswers().keySet())
//                ans.put(key, a.getAnswers().get(key));
//
//        j.put("answers", ans);
//
//        return j;
//    }
//
//    private QuizAttempt jsonToAttempt(JSONObject j) {
//        QuizAttempt a = new QuizAttempt();
//
//        a.setAttemptId(j.getString("attemptId"));
//        a.setQuizId(j.getString("quizId"));
//        a.setStudentId(j.getString("studentId"));
//        a.setCourseId(j.optString("courseId"));
//        a.setLessonId(j.optString("lessonId"));
//        a.setScore(j.getInt("score"));
//        a.setMaxScore(j.getInt("maxScore"));
//        a.setPercentage(j.getDouble("percentage"));
//        a.setPassed(j.getBoolean("passed"));
//        a.setAttemptDate(j.getString("attemptDate"));
//
//        JSONObject ans = j.optJSONObject("answers");
//        if (ans != null) {
//            Map<String,Integer> map = new HashMap<>();
//            for (String key : ans.keySet())
//                map.put(key, ans.getInt(key));
//            a.setAnswers(map);
//        }
//
//        return a;
//    }
//
//    // ---------- CERTIFICATE JSON ----------
//
//    private JSONObject certificateToJson(Certificate c) {
//        JSONObject j = new JSONObject();
//
//        j.put("certificateId", c.getCertificateId());
//        j.put("studentId", c.getStudentId());
//        j.put("courseId", c.getCourseId());
//        j.put("studentName", c.getStudentName());
//        j.put("courseName", c.getCourseName());
//        j.put("instructorName", c.getInstructorName());
//        j.put("issueDate", c.getIssueDate());
//        j.put("finalScore", c.getFinalScore());
//        j.put("totalLessons", c.getTotalLessons());
//        j.put("completedLessons", c.getCompletedLessons());
//
//        return j;
//    }
//
//    private Certificate jsonToCertificate(JSONObject j) {
//        Certificate c = new Certificate();
//
//        c.setCertificateId(j.getString("certificateId"));
//        c.setStudentId(j.getString("studentId"));
//        c.setCourseId(j.getString("courseId"));
//        c.setStudentName(j.optString("studentName", ""));
//        c.setCourseName(j.optString("courseName", ""));
//        c.setInstructorName(j.optString("instructorName", ""));
//        c.setIssueDate(j.getString("issueDate"));
//        c.setFinalScore(j.getDouble("finalScore"));
//        c.setTotalLessons(j.getInt("totalLessons"));
//        c.setCompletedLessons(j.getInt("completedLessons"));
//
//        return c;
//    }
//}
