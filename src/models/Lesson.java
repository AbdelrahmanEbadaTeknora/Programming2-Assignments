package models;

public class Lesson {
    private String lessonId;
    private String title;
    private String content;
    private String[] resources;
    private String quizId;              //new
    private boolean requiresQuizPass; //new

    public Lesson() {
        this.resources = new String[0];
        this.quizId = null;
        this.requiresQuizPass = false;
    }

    public Lesson(String lessonId, String title, String content) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.resources = new String[0];
        this.quizId = null;
        this.requiresQuizPass = false;
    }

    public Lesson(String lessonId, String title, String content, String[] resources) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.resources = resources != null ? resources : new String[0];
        this.quizId = null;
        this.requiresQuizPass = false;
    }

    public Lesson(String lessonId, String title, String content, String[] resources,
                  String quizId, boolean requiresQuizPass) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.resources = resources != null ? resources : new String[0];
        this.quizId = quizId;
        this.requiresQuizPass = requiresQuizPass;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getResources() {
        return resources;
    }

    public void setResources(String[] resources) {
        this.resources = resources != null ? resources : new String[0];
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public boolean isRequiresQuizPass() {
        return requiresQuizPass;
    }

    public void setRequiresQuizPass(boolean requiresQuizPass) {
        this.requiresQuizPass = requiresQuizPass;
    }

    //new methods

    public boolean hasQuiz() {
        return this.quizId != null && !this.quizId.isEmpty();
    }

    public void removeQuiz() {
        this.quizId = null;
        this.requiresQuizPass = false;
    }

    //old

    public void addResource(String resource) {
        String[] newResources = new String[resources.length + 1];
        System.arraycopy(resources, 0, newResources, 0, resources.length);
        newResources[resources.length] = resource;
        this.resources = newResources;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "lessonId='" + lessonId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", resources=" + java.util.Arrays.toString(resources) +
                ", quizId='" + quizId + '\'' +
                ", requiresQuizPass=" + requiresQuizPass +
                '}';
    }
}