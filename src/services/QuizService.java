package services;

import database.JsonDatabaseManager;
import models.Course;
import models.Lesson;
import models.Question;
import models.Quiz;
import models.QuizAttempt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizService {
    private JsonDatabaseManager db;

    // ==================== CONSTRUCTOR ====================

    public QuizService() {
        this.db = JsonDatabaseManager.getInstance();
    }

    // ==================== QUIZ MANAGEMENT ====================

    public boolean createQuiz(String lessonId, Quiz quiz) {
        if (quiz == null || lessonId == null) {
            return false;
        }

        quiz.setLessonId(lessonId);

        // Save quiz to database
        boolean saved = db.saveQuiz(quiz);

        if (saved)
        {
            // Update the lesson to reference this quiz
            // Find the course containing this lesson and update it
            List<Course> allCourses = db.getAllCourses();
            for (Course course : allCourses)
            {
                Lesson[] lessons = course.getLessons();
                if (lessons != null)
                {
                    for (Lesson lesson : lessons)
                    {
                        if (lesson.getLessonId().equals(lessonId))
                        {
                            lesson.setQuizId(quiz.getQuizId());
                            db.updateCourse(course);
                            return true;
                        }
                    }
                }
            }
        }

        return saved;
    }

    public boolean updateQuiz(Quiz quiz) {
        if (quiz == null || quiz.getQuizId() == null) {
            return false;
        }
        return db.updateQuiz(quiz);
    }

    public boolean deleteQuiz(String quizId) {
        if (quizId == null) {
            return false;
        }

        // Get quiz to find lesson
        Quiz quiz = db.getQuizById(quizId);
        if (quiz != null) {
            // Remove quiz reference from lesson
            List<Course> allCourses = db.getAllCourses();
            for (Course course : allCourses) {
                Lesson[] lessons = course.getLessons();
                if (lessons != null) {
                    for (Lesson lesson : lessons) {
                        if (lesson.getLessonId().equals(quiz.getLessonId())) {
                            lesson.setQuizId(null);
                            lesson.setRequiresQuizPass(false);
                            db.updateCourse(course);
                            break;
                        }
                    }
                }
            }
        }

        return db.deleteQuiz(quizId);
    }

    public Quiz getQuizById(String quizId) {
        return db.getQuizById(quizId);
    }

    public Quiz getQuizByLessonId(String lessonId) {
        return db.getQuizByLessonId(lessonId);
    }

    // ==================== QUIZ TAKING ====================

    public QuizAttempt submitQuiz(String studentId, String quizId,
                                  Map<String, Integer> answers) {
        Quiz quiz = db.getQuizById(quizId);
        if (quiz == null) {
            return null;
        }

        // Check if student can still attempt
        if (!canRetakeQuiz(studentId, quizId)) {
            return null;
        }

        // Find courseId and lessonId
        String courseId = null;
        String lessonId = quiz.getLessonId();

        List<Course> allCourses = db.getAllCourses();
        for (Course course : allCourses) {
            Lesson[] lessons = course.getLessons();
            if (lessons != null) {
                for (Lesson lesson : lessons) {
                    if (lesson.getLessonId().equals(lessonId)) {
                        courseId = course.getCourseId();
                        break;
                    }
                }
            }
            if (courseId != null) break;
        }

        // Create attempt
        QuizAttempt attempt = new QuizAttempt(quizId, studentId, courseId, lessonId);
        attempt.setAnswers(answers);

        // Calculate score
        int score = calculateScore(quiz, answers);
        attempt.setScore(score);
        attempt.setMaxScore(quiz.getTotalPoints());
        attempt.evaluate(quiz.getPassingScore());

        // Save attempt
        db.saveQuizAttempt(attempt);

        // If passed and lesson requires quiz, mark lesson as completed
        if (attempt.isPassed()) {
            Lesson lesson = getLessonById(lessonId);
            if (lesson != null && lesson.isRequiresQuizPass()) {
                db.markLessonCompleted(studentId, courseId, lessonId);
            }
        }

        return attempt;
    }

    public int calculateScore(Quiz quiz, Map<String, Integer> answers) {
        if (quiz == null || answers == null) {
            return 0;
        }

        int totalScore = 0;

        for (Question question : quiz.getQuestions()) {
            Integer selectedIndex = answers.get(question.getQuestionId());
            if (selectedIndex != null && question.isCorrect(selectedIndex)) {
                totalScore += question.getPoints();
            }
        }

        return totalScore;
    }

    public Map<String, Boolean> evaluateAnswers(Quiz quiz, Map<String, Integer> answers) {
        Map<String, Boolean> results = new HashMap<>();

        if (quiz == null || answers == null) {
            return results;
        }

        for (Question question : quiz.getQuestions()) {
            Integer selectedIndex = answers.get(question.getQuestionId());
            if (selectedIndex != null) {
                results.put(question.getQuestionId(), question.isCorrect(selectedIndex));
            } else {
                results.put(question.getQuestionId(), false);
            }
        }

        return results;
    }

    public boolean hasPassedQuiz(String studentId, String quizId) {
        List<QuizAttempt> attempts = db.getQuizAttempts(studentId, quizId);

        for (QuizAttempt attempt : attempts) {
            if (attempt.isPassed()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasPassedQuizForLesson(String studentId, String lessonId) {
        Quiz quiz = db.getQuizByLessonId(lessonId);
        if (quiz == null) {
            return true; // No quiz means no requirement
        }
        return hasPassedQuiz(studentId, quiz.getQuizId());
    }

    // ==================== ATTEMPT MANAGEMENT ====================

    public List<QuizAttempt> getStudentAttempts(String studentId, String quizId) {
        return db.getQuizAttempts(studentId, quizId);
    }

    public int getAttemptCount(String studentId, String quizId) {
        List<QuizAttempt> attempts = db.getQuizAttempts(studentId, quizId);
        return attempts != null ? attempts.size() : 0;
    }

    public int getRemainingAttempts(String studentId, String quizId) {
        Quiz quiz = db.getQuizById(quizId);
        if (quiz == null) {
            return 0;
        }

        // Unlimited attempts
        if (quiz.getMaxAttempts() <= 0) {
            return -1; // -1 means unlimited
        }

        int usedAttempts = getAttemptCount(studentId, quizId);
        int remaining = quiz.getMaxAttempts() - usedAttempts;

        return Math.max(0, remaining);
    }

    public QuizAttempt getBestAttempt(String studentId, String quizId) {
        return db.getBestAttempt(studentId, quizId);
    }

    public QuizAttempt getLatestAttempt(String studentId, String quizId) {
        List<QuizAttempt> attempts = db.getQuizAttempts(studentId, quizId);

        if (attempts == null || attempts.isEmpty()) {
            return null;
        }

        // Return the last attempt (most recent)
        return attempts.get(attempts.size() - 1);
    }

    public boolean canRetakeQuiz(String studentId, String quizId) {
        // If already passed, no need to retake
        if (hasPassedQuiz(studentId, quizId)) {
            return false;
        }

        int remaining = getRemainingAttempts(studentId, quizId);

        // -1 means unlimited, otherwise check if remaining > 0
        return remaining == -1 || remaining > 0;
    }

    // ==================== HELPER METHODS ====================

    private Lesson getLessonById(String lessonId) {
        List<Course> allCourses = db.getAllCourses();
        for (Course course : allCourses) {
            Lesson[] lessons = course.getLessons();
            if (lessons != null) {
                for (Lesson lesson : lessons) {
                    if (lesson.getLessonId().equals(lessonId)) {
                        return lesson;
                    }
                }
            }
        }
        return null;
    }

    // ==================== ANALYTICS HELPERS ====================

    public double getAverageScoreForQuiz(String quizId) {
        List<QuizAttempt> allAttempts = db.getAllAttemptsForQuiz(quizId);

        if (allAttempts == null || allAttempts.isEmpty()) {
            return 0;
        }

        double totalPercentage = 0;
        for (QuizAttempt attempt : allAttempts) {
            totalPercentage += attempt.getPercentage();
        }

        return totalPercentage / allAttempts.size();
    }

    public double getPassRateForQuiz(String quizId) {
        List<QuizAttempt> allAttempts = db.getAllAttemptsForQuiz(quizId);

        if (allAttempts == null || allAttempts.isEmpty()) {
            return 0;
        }

        int passedCount = 0;
        for (QuizAttempt attempt : allAttempts) {
            if (attempt.isPassed()) {
                passedCount++;
            }
        }

        return (passedCount / (double) allAttempts.size()) * 100;
    }

    public int getTotalAttemptsForQuiz(String quizId) {
        List<QuizAttempt> allAttempts = db.getAllAttemptsForQuiz(quizId);
        return allAttempts != null ? allAttempts.size() : 0;
    }
}