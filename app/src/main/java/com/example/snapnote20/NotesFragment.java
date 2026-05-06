package com.example.snapnote20;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

// Fragment showing list of notes
public class NotesFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private DatabaseHelper dbHelper;
    private EditText etSearch;
    private ChipGroup chipGroup;
    private FloatingActionButton fabAddNote;
    private String selectedCategory = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Using activity_notes_fragment.xml as fragment_notes.xml doesn't exist
        View view = inflater.inflate(R.layout.activity_notes_fragment, container, false);

        dbHelper = new DatabaseHelper(getContext());

        // Find views
        recyclerView = view.findViewById(R.id.recyclerViewNotes);
        etSearch = view.findViewById(R.id.etSearch);
        chipGroup = view.findViewById(R.id.chipGroup);
        fabAddNote = view.findViewById(R.id.fabAddNote);

        // Setup RecyclerView with staggered grid (2 columns)
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        // Load notes and set adapter
        noteList = dbHelper.getAllNotes();
        noteAdapter = new NoteAdapter(getContext(), noteList);
        recyclerView.setAdapter(noteAdapter);

        // FAB click: open AddNoteActivity
        fabAddNote.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddNoteActivity.class));
        });

        // Search bar: filter notes in real time
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString());
            }
        });

        // Chip selection for category filter
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip chip = group.findViewById(checkedIds.get(0));
                if (chip != null) {
                    selectedCategory = chip.getText().toString();
                    loadNotesByCategory(selectedCategory);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh notes when returning from AddNoteActivity
        loadNotesByCategory(selectedCategory);
    }

    // Load notes filtered by category
    private void loadNotesByCategory(String category) {
        if ("All".equals(category)) {
            noteList = dbHelper.getAllNotes();
        } else {
            noteList = dbHelper.getNotesByCategory(category);
        }
        noteAdapter.updateList(noteList);
    }

    // Filter notes by search text
    private void filterNotes(String query) {
        List<Note> allNotes = "All".equals(selectedCategory)
                ? dbHelper.getAllNotes()
                : dbHelper.getNotesByCategory(selectedCategory);

        if (query.isEmpty()) {
            noteAdapter.updateList(allNotes);
            return;
        }

        List<Note> filtered = new ArrayList<>();
        for (Note note : allNotes) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase())
                    || (note.getContent() != null && note.getContent().toLowerCase().contains(query.toLowerCase()))) {
                filtered.add(note);
            }
        }
        noteAdapter.updateList(filtered);
    }
}
