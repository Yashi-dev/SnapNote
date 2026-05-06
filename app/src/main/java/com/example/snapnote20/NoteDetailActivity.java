package com.example.snapnote20;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.chip.Chip;

// Activity showing full details of a note
public class NoteDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvContent, tvDate;
    private Chip chipCategory, chipPriority;
    private ImageView ivNoteImage;
    private int noteId;
    private String noteTitle, noteContent, noteCategory, notePriority, noteDate, noteImage;
    private int notePinned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Note Detail");
        }

        // Get data passed from NoteAdapter or DB
        noteId = getIntent().getIntExtra("note_id", -1);
        
        // Find views
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvContent = findViewById(R.id.tvDetailContent);
        tvDate = findViewById(R.id.tvDetailDate);
        chipCategory = findViewById(R.id.chipDetailCategory);
        chipPriority = findViewById(R.id.chipDetailPriority);
        ivNoteImage = findViewById(R.id.ivDetailImage);

        // Ideally we should load data from DB using noteId here for accuracy
        // But for now, we'll try to get from Intent as well
        noteTitle = getIntent().getStringExtra("note_title");
        noteContent = getIntent().getStringExtra("note_content");
        noteCategory = getIntent().getStringExtra("note_category");
        notePriority = getIntent().getStringExtra("note_priority");
        noteDate = getIntent().getStringExtra("note_date");
        noteImage = getIntent().getStringExtra("note_image");
        notePinned = getIntent().getIntExtra("note_pinned", 0);

        // Set data
        tvTitle.setText(noteTitle);
        tvContent.setText(noteContent);
        tvDate.setText(noteDate);
        chipCategory.setText(noteCategory);
        chipPriority.setText(notePriority);

        // Show image if available
        if (noteImage != null && !noteImage.isEmpty()) {
            ivNoteImage.setVisibility(android.view.View.VISIBLE);
            ivNoteImage.setImageURI(Uri.parse(noteImage));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_edit) {
            // Open AddNoteActivity in edit mode
            Intent intent = new Intent(this, AddNoteActivity.class);
            intent.putExtra("edit_mode", true);
            intent.putExtra("note_id", noteId);
            intent.putExtra("note_title", noteTitle);
            intent.putExtra("note_content", noteContent);
            intent.putExtra("note_category", noteCategory);
            intent.putExtra("note_priority", notePriority);
            intent.putExtra("note_image", noteImage);
            startActivity(intent);
            return true;

        } else if (id == R.id.action_share) {
            // Share note via ACTION_SEND
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, noteTitle);
            shareIntent.putExtra(Intent.EXTRA_TEXT, noteTitle + "\n\n" + noteContent);
            startActivity(Intent.createChooser(shareIntent, "Share Note"));
            return true;

        } else if (id == R.id.action_delete) {
            // Delete note and go back
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.deleteNote(noteId);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
