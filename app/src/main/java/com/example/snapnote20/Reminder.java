package com.example.snapnote20;

// Model class for a Reminder
public class Reminder {
    private int id;
    private int noteId;
    private String title;
    private String phone;
    private String email;
    private String website;
    private String date;
    private String time;
    private String actionType;

    public Reminder() {}

    public Reminder(int id, int noteId, String title, String phone,
                    String email, String website, String date,
                    String time, String actionType) {
        this.id = id;
        this.noteId = noteId;
        this.title = title;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.date = date;
        this.time = time;
        this.actionType = actionType;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNoteId() { return noteId; }
    public void setNoteId(int noteId) { this.noteId = noteId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
}