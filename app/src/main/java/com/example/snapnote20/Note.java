package com.example.snapnote20;

// Model class for a Note
public class Note {
    private int id;
    private String title;
    private String content;
    private String category;
    private String imagePath;
    private String audioPath;
    private String createdAt;
    private int isPinned;
    private String priority;

    public Note() {}

    public Note(int id, String title, String content, String category,
                String imagePath, String audioPath, String createdAt,
                int isPinned, String priority) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.imagePath = imagePath;
        this.audioPath = audioPath;
        this.createdAt = createdAt;
        this.isPinned = isPinned;
        this.priority = priority;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getAudioPath() { return audioPath; }
    public void setAudioPath(String audioPath) { this.audioPath = audioPath; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public int getIsPinned() { return isPinned; }
    public void setIsPinned(int isPinned) { this.isPinned = isPinned; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}