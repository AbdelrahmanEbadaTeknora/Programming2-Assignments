package models;

import java.util.UUID;

public class Question {
    private String questionId;
    private String questionText;
    private String[] options;
    private int correctOptionIndex;
    private int points;


    public Question() {
        this.questionId = generateQuestionId();
        this.options = new String[4];
        this.correctOptionIndex = 0;
        this.points = 10;
    }

    public Question(String questionId, String questionText, String[] options,
                    int correctOptionIndex, int points) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.options = options != null ? options : new String[4];
        this.correctOptionIndex = correctOptionIndex;
        this.points = points;
    }

    public Question(String questionText, String[] options, int correctOptionIndex, int points) {
        this.questionId = generateQuestionId();
        this.questionText = questionText;
        this.options = options != null ? options : new String[4];
        this.correctOptionIndex = correctOptionIndex;
        this.points = points;
    }


    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options != null ? options : new String[4];
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public void setCorrectOptionIndex(int correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == this.correctOptionIndex;
    }

    public String getCorrectAnswer() {
        if (options != null && correctOptionIndex >= 0 && correctOptionIndex < options.length) {
            return options[correctOptionIndex];
        }
        return null;
    }

    public String getOption(int index) {
        if (options != null && index >= 0 && index < options.length) {
            return options[index];
        }
        return null;
    }

    public void setOption(int index, String optionText) {
        if (options != null && index >= 0 && index < options.length) {
            options[index] = optionText;
        }
    }

    public int getOptionsCount() {
        return options != null ? options.length : 0;
    }

    public boolean isValid() {
        if (questionText == null || questionText.trim().isEmpty()) {
            return false;
        }
        if (options == null || options.length < 2) {
            return false;
        }
        if (correctOptionIndex < 0 || correctOptionIndex >= options.length) {
            return false;
        }
        if (points <= 0) {
            return false;
        }
        // Check that at least 2 options are non-empty
        int nonEmptyCount = 0;
        for (String option : options) {
            if (option != null && !option.trim().isEmpty()) {
                nonEmptyCount++;
            }
        }
        return nonEmptyCount >= 2;
    }

    private String generateQuestionId() {
        return "QN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId='" + questionId + '\'' +
                ", questionText='" + questionText + '\'' +
                ", optionsCount=" + getOptionsCount() +
                ", correctOptionIndex=" + correctOptionIndex +
                ", points=" + points +
                '}';
    }
}