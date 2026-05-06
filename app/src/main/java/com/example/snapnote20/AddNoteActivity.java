package com.example.snapnote20;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Activity to add a new note or edit an existing one
public class AddNoteActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 101;

    private TextInputEditText etTitle, etContent;
    private AutoCompleteTextView spinnerCategory, spinnerPriority;
    private MaterialButton btnSave, btnPickImage;
    private ImageView ivImagePreview;
    private Toolbar toolbar;
    private DatabaseHelper dbHelper;

    private boolean isEditMode = false;
    private int noteId = -1;
    private String selectedImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        dbHelper = new DatabaseHelper(this);

        // Setup toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Find views
        etTitle = findViewById(R.id.etNoteTitle);
        etContent = findViewById(R.id.etNoteContent);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        btnSave = findViewById(R.id.btnSaveNote);
        btnPickImage = findViewById(R.id.btnPickImage);
        ivImagePreview = findViewById(R.id.ivImagePreview);

        // Setup category dropdown
        String[] categories = {"Study", "Personal", "Work", "Important"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        spinnerCategory.setAdapter(catAdapter);
        spinnerCategory.setText("Study", false);

        // Setup priority dropdown
        String[] priorities = {"Low", "Medium", "High"};
        ArrayAdapter<String> priAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, priorities);
        spinnerPriority.setAdapter(priAdapter);
        spinnerPriority.setText("Medium", false);

        // Check if in edit mode
        isEditMode = getIntent().getBooleanExtra("edit_mode", false);
        if (isEditMode) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Edit Note");
            }
            noteId = getIntent().getIntExtra("note_id", -1);
            etTitle.setText(getIntent().getStringExtra("note_title"));
            etContent.setText(getIntent().getStringExtra("note_content"));
            spinnerCategory.setText(getIntent().getStringExtra("note_category"), false);
            spinnerPriority.setText(getIntent().getStringExtra("note_priority"), false);
            selectedImagePath = getIntent().getStringExtra("note_image") != null
                    ? getIntent().getStringExtra("note_image") : "";
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("New Note");
            }
        }

        // Pick image from gallery
        btnPickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Save button click
        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();
            String category = spinnerCategory.getText().toString().trim();
            String priority = spinnerPriority.getText().toString().trim();

            // Validate
            if (TextUtils.isEmpty(title)) {
                etTitle.setError("Title is required");
                return;
            }
            if (TextUtils.isEmpty(content)) {
                etContent.setError("Content is required");
                return;
            }

            // Get current date/time
            String dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(new Date());

            Note note = new Note();
            note.setTitle(title);
            note.setContent(content);
            note.setCategory(category);
            note.setImagePath(selectedImagePath);
            note.setAudioPath("");
            note.setCreatedAt(dateTime);
            note.setIsPinned(0);
            note.setPriority(priority);

            if (isEditMode && noteId != -1) {
                note.setId(noteId);
                dbHelper.updateNote(note);
                Snackbar.make(v, "Note updated!", Snackbar.LENGTH_SHORT).show();
            } else {
                dbHelper.addNote(note); // Changed from insertNote to addNote
                Snackbar.make(v, "Note saved!", Snackbar.LENGTH_SHORT).show();
            }

            // Go back after short delay
            new android.os.Handler().postDelayed(this::finish, 800);
        });
    }

    // Handle image picker result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                selectedImagePath = imageUri.toString();
                ivImagePreview.setImageURI(imageUri);
                ivImagePreview.setVisibility(android.view.View.VISIBLE);
            }
        }
    }

    // Handle back arrow in toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
