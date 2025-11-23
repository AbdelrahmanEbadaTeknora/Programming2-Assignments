package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QuizAttempt {
    private String attemptId;
    private String quizId;
    private String studentId;
    private String courseId;
    private String lessonId;
    private Map<String, Integer> answers;
    private int score;
    private int maxScore;
    private double percentage;
    private boolean passed;
    private String attemptDate;


    public QuizAttempt() {
        this.attemptId = generateAttemptId();
        this.answers = new HashMap<>();
        this.attemptDate = getCurrentDateTime();
    }

    public QuizAttempt(String attemptId, String quizId, String studentId) {
        this.attemptId = attemptId;
        this.quizId = quizId;
        this.studentId = studentId;
        this.answers = new HashMap<>();
        this.attemptDate = getCurrentDateTime();
    }

    public QuizAttempt(String quizId, String studentId, String courseId, String lessonId) {
        this.attemptId = generateAttemptId();
        this.quizId = quizId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.answers = new HashMap<>();
        this.attemptDate = getCurrentDateTime();
    }


    public String getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(String attemptId) {
        this.attemptId = attemptId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public Map<String, Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, Integer> answers) {
        this.answers = answers != null ? answers : new HashMap<>();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getAttemptDate() {
        return attemptDate;
    }

    public void setAttemptDate(String attemptDate) {
        this.attemptDate = attemptDate;
    }


    public void addAnswer(String questionId, int selectedIndex) {
        if (this.answers == null) {
            this.answers = new HashMap<>();
        }
        this.answers.put(questionId, selectedIndex);
    }

    public Integer getAnswer(String questionId) {
        if (this.answers == null) {
            return null;
        }
        return this.answers.get(questionId);
    }

    public boolean hasAnswered(String questionId) {
        return this.answers != null && this.answers.containsKey(questionId);
    }

    public int getAnsweredCount() {
        return this.answers != null ? this.answers.size() : 0;
    }

    public void calculatePercentage() {
        if (this.maxScore > 0) {
            this.percentage = (this.score / (double) this.maxScore) * 100;
        } else {
            this.percentage = 0;
        }
    }

    public void evaluate(int passingScorePercentage) {
        calculatePercentage();
        this.passed = this.percentage >= passingScorePercentage;
    }

    public void clearAnswers() {
        if (this.answers != null) {
            this.answers.clear();
        }
    }

    private String generateAttemptId() {
        return "ATT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public String getFormattedPercentage() {
        return String.format("%.1f%%", this.percentage);
    }

    @Override
    public String toString() {
        return "QuizAttempt{" +
                "attemptId='" + attemptId + '\'' +
                ", quizId='" + quizId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", score=" + score + "/" + maxScore +
                ", percentage=" + getFormattedPercentage() +
                ", passed=" + passed +
                ", attemptDate='" + attemptDate + '\'' +
                '}';
    }
}