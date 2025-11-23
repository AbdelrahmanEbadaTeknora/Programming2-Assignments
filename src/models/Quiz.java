package models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Quiz {
    private String quizId;
    private String lessonId;
    private String title;
    private List<Question> questions;
    private int passingScore;
    private int maxAttempts;
    private int timeLimit;

    public Quiz() {
        this.quizId = generateQuizId();
        this.questions = new ArrayList<>();
        this.passingScore = 70;
        this.maxAttempts = 3;
        this.timeLimit = -1;
    }

    public Quiz(String quizId, String lessonId, String title) {
        this.quizId = quizId;
        this.lessonId = lessonId;
        this.title = title;
        this.questions = new ArrayList<>();
        this.passingScore = 70;
        this.maxAttempts = 3;
        this.timeLimit = -1;
    }

    public Quiz(String lessonId, String title, int passingScore, int maxAttempts, int timeLimit) {
        this.quizId = generateQuizId();
        this.lessonId = lessonId;
        this.title = title;
        this.questions = new ArrayList<>();
        this.passingScore = passingScore;
        this.maxAttempts = maxAttempts;
        this.timeLimit = timeLimit;
    }


    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions != null ? questions : new ArrayList<>();
    }

    public int getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(int passingScore) {
        this.passingScore = passingScore;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }


    public void addQuestion(Question question) {
        if (this.questions == null) {
            this.questions = new ArrayList<>();
        }
        this.questions.add(question);
    }

    public boolean removeQuestion(String questionId) {
        if (this.questions == null) {
            return false;
        }
        return this.questions.removeIf(q -> q.getQuestionId().equals(questionId));
    }

    public Question getQuestionById(String questionId) {
        if (this.questions == null) {
            return null;
        }
        for (Question q : this.questions) {
            if (q.getQuestionId().equals(questionId)) {
                return q;
            }
        }
        return null;
    }

    public int getTotalPoints() {
        if (this.questions == null || this.questions.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (Question q : this.questions) {
            total += q.getPoints();
        }
        return total;
    }

    public int getQuestionCount() {
        return this.questions != null ? this.questions.size() : 0;
    }

    public boolean hasTimeLimit() {
        return this.timeLimit > 0;
    }

    public boolean hasAttemptLimit() {
        return this.maxAttempts > 0;
    }

    private String generateQuizId() {
        return "QZ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "quizId='" + quizId + '\'' +
                ", lessonId='" + lessonId + '\'' +
                ", title='" + title + '\'' +
                ", questions=" + getQuestionCount() +
                ", passingScore=" + passingScore +
                ", maxAttempts=" + maxAttempts +
                ", timeLimit=" + timeLimit +
                '}';
    }
}