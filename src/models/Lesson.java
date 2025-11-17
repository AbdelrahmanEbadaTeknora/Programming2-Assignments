package models; // or your package structure

public class Lesson {
    private String lessonId;
    private String title;
    private String content;
    private String[] resources; // Optional: URLs, file paths, etc.

    // Constructors
    public Lesson() {
        this.resources = new String[0];
    }


    public Lesson(String lessonId, String title, String content) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.resources = new String[0];
    }

    public Lesson(String lessonId, String title, String content, String[] resources) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.resources = resources != null ? resources : new String[0];
    }

    // Getters and Setters
    public String getLessonId() { return lessonId; }
    public void setLessonId(String lessonId) { this.lessonId = lessonId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String[] getResources() { return resources; }
    public void setResources(String[] resources) {
        this.resources = resources != null ? resources : new String[0];
    }

    // Utility method
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
                '}';
    }
}