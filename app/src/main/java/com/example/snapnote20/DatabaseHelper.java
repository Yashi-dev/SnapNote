package com.example.snapnote20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SnapNote.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_NOTES = "notes";
    private static final String TABLE_REMINDERS = "reminders";

    // Common column names
    private static final String KEY_ID = "id";

    // NOTES Table - column names
    private static final String KEY_NOTE_TITLE = "title";
    private static final String KEY_NOTE_CONTENT = "content";
    private static final String KEY_NOTE_CATEGORY = "category";
    private static final String KEY_NOTE_IMAGE_PATH = "imagePath";
    private static final String KEY_NOTE_AUDIO_PATH = "audioPath";
    private static final String KEY_NOTE_CREATED_AT = "createdAt";
    private static final String KEY_NOTE_IS_PINNED = "isPinned";
    private static final String KEY_NOTE_PRIORITY = "priority";

    // REMINDERS Table - column names
    private static final String KEY_REMINDER_NOTE_ID = "noteId";
    private static final String KEY_REMINDER_TITLE = "title";
    private static final String KEY_REMINDER_PHONE = "phone";
    private static final String KEY_REMINDER_EMAIL = "email";
    private static final String KEY_REMINDER_WEBSITE = "website";
    private static final String KEY_REMINDER_DATE = "date";
    private static final String KEY_REMINDER_TIME = "time";
    private static final String KEY_REMINDER_ACTION_TYPE = "actionType";

    // Table Create Statements
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE " + TABLE_NOTES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NOTE_TITLE + " TEXT,"
            + KEY_NOTE_CONTENT + " TEXT,"
            + KEY_NOTE_CATEGORY + " TEXT,"
            + KEY_NOTE_IMAGE_PATH + " TEXT,"
            + KEY_NOTE_AUDIO_PATH + " TEXT,"
            + KEY_NOTE_CREATED_AT + " TEXT,"
            + KEY_NOTE_IS_PINNED + " INTEGER,"
            + KEY_NOTE_PRIORITY + " TEXT" + ")";

    private static final String CREATE_TABLE_REMINDERS = "CREATE TABLE " + TABLE_REMINDERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_REMINDER_NOTE_ID + " INTEGER,"
            + KEY_REMINDER_TITLE + " TEXT,"
            + KEY_REMINDER_PHONE + " TEXT,"
            + KEY_REMINDER_EMAIL + " TEXT,"
            + KEY_REMINDER_WEBSITE + " TEXT,"
            + KEY_REMINDER_DATE + " TEXT,"
            + KEY_REMINDER_TIME + " TEXT,"
            + KEY_REMINDER_ACTION_TYPE + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL(CREATE_TABLE_REMINDERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        onCreate(db);
    }

    // --- NOTE CRUD Operations ---

    public long addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOTE_TITLE, note.getTitle());
        values.put(KEY_NOTE_CONTENT, note.getContent());
        values.put(KEY_NOTE_CATEGORY, note.getCategory());
        values.put(KEY_NOTE_IMAGE_PATH, note.getImagePath());
        values.put(KEY_NOTE_AUDIO_PATH, note.getAudioPath());
        values.put(KEY_NOTE_CREATED_AT, note.getCreatedAt());
        values.put(KEY_NOTE_IS_PINNED, note.getIsPinned());
        values.put(KEY_NOTE_PRIORITY, note.getPriority());
        return db.insert(TABLE_NOTES, null, values);
    }

    public List<Note> getAllNotes() {
        return getNotes(null, null);
    }

    public List<Note> getNotesByCategory(String category) {
        return getNotes(KEY_NOTE_CATEGORY + " = ?", new String[]{category});
    }

    private List<Note> getNotes(String selection, String[] selectionArgs) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String orderBy = KEY_NOTE_IS_PINNED + " DESC, " + KEY_ID + " DESC";
        Cursor cursor = db.query(TABLE_NOTES, null, selection, selectionArgs, null, null, orderBy);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTE_TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTE_CONTENT)));
                note.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTE_CATEGORY)));
                note.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTE_IMAGE_PATH)));
                note.setAudioPath(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTE_AUDIO_PATH)));
                note.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTE_CREATED_AT)));
                note.setIsPinned(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NOTE_IS_PINNED)));
                note.setPriority(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTE_PRIORITY)));
                notes.add(note);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return notes;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOTE_TITLE, note.getTitle());
        values.put(KEY_NOTE_CONTENT, note.getContent());
        values.put(KEY_NOTE_CATEGORY, note.getCategory());
        values.put(KEY_NOTE_IMAGE_PATH, note.getImagePath());
        values.put(KEY_NOTE_AUDIO_PATH, note.getAudioPath());
        values.put(KEY_NOTE_CREATED_AT, note.getCreatedAt());
        values.put(KEY_NOTE_IS_PINNED, note.getIsPinned());
        values.put(KEY_NOTE_PRIORITY, note.getPriority());
        return db.update(TABLE_NOTES, values, KEY_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_REMINDERS, KEY_REMINDER_NOTE_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // --- Stats Methods ---

    public long getNotesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_NOTES);
    }

    public long getReminderCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_REMINDERS);
    }

    public long getNotesTodayCount(String todayDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_NOTES, KEY_NOTE_CREATED_AT + " LIKE ?", new String[]{todayDate + "%"});
    }

    // --- REMINDER CRUD Operations ---

    public long addReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REMINDER_NOTE_ID, reminder.getNoteId());
        values.put(KEY_REMINDER_TITLE, reminder.getTitle());
        values.put(KEY_REMINDER_PHONE, reminder.getPhone());
        values.put(KEY_REMINDER_EMAIL, reminder.getEmail());
        values.put(KEY_REMINDER_WEBSITE, reminder.getWebsite());
        values.put(KEY_REMINDER_DATE, reminder.getDate());
        values.put(KEY_REMINDER_TIME, reminder.getTime());
        values.put(KEY_REMINDER_ACTION_TYPE, reminder.getActionType());
        return db.insert(TABLE_REMINDERS, null, values);
    }

    public List<Reminder> getAllReminders() {
        List<Reminder> reminders = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_REMINDERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                reminder.setNoteId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_REMINDER_NOTE_ID)));
                reminder.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REMINDER_TITLE)));
                reminder.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REMINDER_PHONE)));
                reminder.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REMINDER_EMAIL)));
                reminder.setWebsite(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REMINDER_WEBSITE)));
                reminder.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REMINDER_DATE)));
                reminder.setTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REMINDER_TIME)));
                reminder.setActionType(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REMINDER_ACTION_TYPE)));
                reminders.add(reminder);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return reminders;
    }

    public int updateReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REMINDER_TITLE, reminder.getTitle());
        values.put(KEY_REMINDER_PHONE, reminder.getPhone());
        values.put(KEY_REMINDER_EMAIL, reminder.getEmail());
        values.put(KEY_REMINDER_WEBSITE, reminder.getWebsite());
        values.put(KEY_REMINDER_DATE, reminder.getDate());
        values.put(KEY_REMINDER_TIME, reminder.getTime());
        values.put(KEY_REMINDER_ACTION_TYPE, reminder.getActionType());
        return db.update(TABLE_REMINDERS, values, KEY_ID + " = ?", new String[]{String.valueOf(reminder.getId())});
    }

    public void deleteReminder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDERS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
