package services;

import database.JsonDatabaseManager;
import java.util.*;
import models.*;

public class AnalyticsService {
    private JsonDatabaseManager dbManager;

    // Constructor
    public AnalyticsService() {
        this.dbManager = JsonDatabaseManager.getInstance();
    }

    /**
     * Get student performance for a course (student ID -> progress percentage)
     */
    public Map<String, Double> getStudentPerformance(String courseId) {
        Map<String, Double> performance = new LinkedHashMap<>();
        Course course = dbManager.getCourseById(courseId);

        if (course == null || course.getStudents() == null) {
            return performance;
        }

        for (String studentId : course.getStudents()) {
            double progress = getStudentProgress(studentId, courseId);
            performance.put(studentId, progress);
        }

        return performance;
    }

    /**
     * Get quiz averages for a course (lesson title -> average score)
     */
    public Map<String, Double> getQuizAverages(String courseId) {
        Map<String, Double> quizAverages = new LinkedHashMap<>();
        Course course = dbManager.getCourseById(courseId);

        if (course == null || course.getLessons() == null) {
            return quizAverages;
        }

        for (Lesson lesson : course.getLessons()) {
            // Only include lessons that have quizzes
            if (lesson.hasQuiz()) {
                double avgScore = getQuizAverageByLesson(lesson.getLessonId());
                quizAverages.put(lesson.getTitle(), avgScore);
            }
        }

        return quizAverages;
    }

    /**
     * Get average quiz score by lesson ID
     */
    public double getQuizAverageByLesson(String lessonId) {
        // Get the quiz for this lesson
        Quiz quiz = dbManager.getQuizByLessonId(lessonId);
        if (quiz == null) {
            return 0.0; // No quiz for this lesson
        }

        // Get all attempts for this quiz
        List<QuizAttempt> allAttempts = dbManager.getAllAttemptsForQuiz(quiz.getQuizId());

        if (allAttempts == null || allAttempts.isEmpty()) {
            return 0.0; // No attempts yet
        }

        // Calculate average of all attempts
        double totalPercentage = 0.0;
        for (QuizAttempt attempt : allAttempts) {
            totalPercentage += attempt.getPercentage();
        }

        return totalPercentage / allAttempts.size();
    }

    /**
     * Get lesson completion rates for a course (lesson title -> completion
     * percentage)
     */
    public Map<String, Double> getLessonCompletionRates(String courseId) {
        Map<String, Double> completionRates = new LinkedHashMap<>();
        Course course = dbManager.getCourseById(courseId);

        if (course == null || course.getLessons() == null || course.getStudents() == null) {
            return completionRates;
        }

        int totalStudents = course.getStudents().size();
        if (totalStudents == 0) {
            for (Lesson lesson : course.getLessons()) {
                completionRates.put(lesson.getTitle(), 0.0);
            }
            return completionRates;
        }

        for (Lesson lesson : course.getLessons()) {
            int completedCount = 0;

            for (String studentId : course.getStudents()) {
                List<String> completedLessons = dbManager.getCompletedLessons(studentId, courseId);
                if (completedLessons.contains(lesson.getLessonId())) {
                    completedCount++;
                }
            }

            double rate = (completedCount * 100.0) / totalStudents;
            completionRates.put(lesson.getTitle(), rate);
        }

        return completionRates;
    }

    /**
     * Get overall course completion rate
     */
    public double getCourseCompletionRate(String courseId) {
        Course course = dbManager.getCourseById(courseId);

        if (course == null || course.getStudents() == null || course.getLessons() == null) {
            return 0.0;
        }

        int totalStudents = course.getStudents().size();
        int totalLessons = course.getLessons().length;

        if (totalStudents == 0 || totalLessons == 0) {
            return 0.0;
        }

        int completedStudents = 0;
        for (String studentId : course.getStudents()) {
            List<String> completedLessons = dbManager.getCompletedLessons(studentId, courseId);
            if (completedLessons.size() >= totalLessons) {
                completedStudents++;
            }
        }

        return (completedStudents * 100.0) / totalStudents;
    }

    /**
     * Get individual student progress in a course
     */
    public double getStudentProgress(String studentId, String courseId) {
        Course course = dbManager.getCourseById(courseId);

        if (course == null || course.getLessons() == null) {
            return 0.0;
        }

        int totalLessons = course.getLessons().length;
        if (totalLessons == 0) {
            return 100.0;
        }

        List<String> completedLessons = dbManager.getCompletedLessons(studentId, courseId);
        return (completedLessons.size() * 100.0) / totalLessons;
    }

    /**
     * Get number of enrolled students in a course
     */
    public int getEnrolledStudentCount(String courseId) {
        Course course = dbManager.getCourseById(courseId);

        if (course == null || course.getStudents() == null) {
            return 0;
        }

        return course.getStudents().size();
    }

    /**
     * Get average quiz score across all quizzes in a course
     */
    // public double getAverageQuizScore(String courseId) {
    //     Map<String, Double> quizAverages = getQuizAverages(courseId);

    //     if (quizAverages.isEmpty()) {
    //         return 0.0;
    //     }

    //     double total = 0;
    //     for (double avg : quizAverages.values()) {
    //         total += avg;
    //     }

    //     return total / quizAverages.size();
    // }

    /**
     * Get average quiz score across all quizzes in a course
     */
    public double getAverageQuizScore(String courseId) {
        Map<String, Double> quizAverages = getQuizAverages(courseId);

        if (quizAverages.isEmpty()) {
            return 0.0;
        }

        double total = 0;
        int count = 0;

        for (double avg : quizAverages.values()) {
            if (avg > 0) { // Only count quizzes that have been attempted
                total += avg;
                count++;
            }
        }

        return count > 0 ? total / count : 0.0;
    }

    /**
     * Get detailed quiz statistics for a course
     */
    public Map<String, Object> getQuizStatistics(String courseId) {
        Map<String, Object> stats = new LinkedHashMap<>();
        Course course = dbManager.getCourseById(courseId);

        if (course == null || course.getLessons() == null) {
            return stats;
        }

        int totalQuizzes = 0;
        int quizzesAttempted = 0;
        int totalAttempts = 0;
        double totalScore = 0.0;
        int passedCount = 0;

        for (Lesson lesson : course.getLessons()) {
            if (lesson.hasQuiz()) {
                totalQuizzes++;

                Quiz quiz = dbManager.getQuizByLessonId(lesson.getLessonId());
                if (quiz != null) {
                    List<QuizAttempt> attempts = dbManager.getAllAttemptsForQuiz(quiz.getQuizId());

                    if (!attempts.isEmpty()) {
                        quizzesAttempted++;
                        totalAttempts += attempts.size();

                        for (QuizAttempt attempt : attempts) {
                            totalScore += attempt.getPercentage();
                            if (attempt.isPassed()) {
                                passedCount++;
                            }
                        }
                    }
                }
            }
        }

        stats.put("totalQuizzes", totalQuizzes);
        stats.put("quizzesAttempted", quizzesAttempted);
        stats.put("totalAttempts", totalAttempts);
        stats.put("averageScore", totalAttempts > 0 ? totalScore / totalAttempts : 0.0);
        stats.put("passRate", totalAttempts > 0 ? (passedCount * 100.0) / totalAttempts : 0.0);

        return stats;
    }

    /**
     * Get quiz performance for all students in a course
     */
    public Map<String, Double> getStudentQuizPerformance(String courseId) {
        Map<String, Double> performance = new LinkedHashMap<>();
        Course course = dbManager.getCourseById(courseId);

        if (course == null || course.getStudents() == null || course.getLessons() == null) {
            return performance;
        }

        for (String studentId : course.getStudents()) {
            User user = dbManager.getUserById(studentId);
            if (!(user instanceof Student))
                continue;

            String studentName = user.getUsername();
            double totalScore = 0.0;
            int quizCount = 0;

            // Get best score for each quiz
            for (Lesson lesson : course.getLessons()) {
                if (lesson.hasQuiz()) {
                    Quiz quiz = dbManager.getQuizByLessonId(lesson.getLessonId());
                    if (quiz != null) {
                        QuizAttempt bestAttempt = dbManager.getBestAttempt(studentId, quiz.getQuizId());
                        if (bestAttempt != null) {
                            totalScore += bestAttempt.getPercentage();
                            quizCount++;
                        }
                    }
                }
            }

            double avgScore = quizCount > 0 ? totalScore / quizCount : 0.0;
            performance.put(studentName, avgScore);
        }

        return performance;
    }

    /**
     * Get top performing students in a course
     */
    public List<Student> getTopPerformingStudents(String courseId, int limit) {
        Map<String, Double> performance = getStudentPerformance(courseId);

        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(performance.entrySet());
        sortedEntries.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        List<Student> topStudents = new ArrayList<>();
        int count = 0;

        for (Map.Entry<String, Double> entry : sortedEntries) {
            if (count >= limit)
                break;

            User user = dbManager.getUserById(entry.getKey());
            if (user instanceof Student) {
                topStudents.add((Student) user);
                count++;
            }
        }

        return topStudents;
    }

    /**
     * Get struggling students (below threshold performance)
     */
    public List<Student> getStrugglingStudents(String courseId, double threshold) {
        Map<String, Double> performance = getStudentPerformance(courseId);
        List<Student> strugglingStudents = new ArrayList<>();

        for (Map.Entry<String, Double> entry : performance.entrySet()) {
            if (entry.getValue() < threshold) {
                User user = dbManager.getUserById(entry.getKey());
                if (user instanceof Student) {
                    strugglingStudents.add((Student) user);
                }
            }
        }

        return strugglingStudents;
    }

    /**
     * Get student names with their performance scores
     */
    public Map<String, Double> getStudentPerformanceWithNames(String courseId) {
        Map<String, Double> performance = new LinkedHashMap<>();
        Course course = dbManager.getCourseById(courseId);

        if (course == null || course.getStudents() == null) {
            return performance;
        }

        for (String studentId : course.getStudents()) {
            User user = dbManager.getUserById(studentId);
            if (user instanceof Student) {
                String name = user.getUsername();
                double progress = getStudentProgress(studentId, courseId);
                performance.put(name, progress);
            }
        }

        return performance;
    }

    /**
     * Get course statistics summary
     */
    public Map<String, Object> getCourseStatsSummary(String courseId) {
        Map<String, Object> stats = new LinkedHashMap<>();
        Course course = dbManager.getCourseById(courseId);

        if (course == null) {
            return stats;
        }

        stats.put("courseTitle", course.getTitle());
        stats.put("totalStudents", getEnrolledStudentCount(courseId));
        stats.put("totalLessons", course.getLessons() != null ? course.getLessons().length : 0);
        stats.put("completionRate", getCourseCompletionRate(courseId));
        stats.put("averageQuizScore", getAverageQuizScore(courseId));

        return stats;
    }
}