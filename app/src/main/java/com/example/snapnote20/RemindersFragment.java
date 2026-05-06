package com.example.snapnote20;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

// Fragment showing all reminders
public class RemindersFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReminderAdapter reminderAdapter;
    private List<Reminder> reminderList;
    private DatabaseHelper dbHelper;
    private TextView tvEmpty;
    private FloatingActionButton fabAddReminder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reminders_fragment, container, false);

        dbHelper = new DatabaseHelper(getContext());

        // Find views
        recyclerView = view.findViewById(R.id.recyclerViewReminders);
        tvEmpty = view.findViewById(R.id.tvEmptyReminders);
        fabAddReminder = view.findViewById(R.id.fabAddReminder);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load reminders
        reminderList = dbHelper.getAllReminders();
        reminderAdapter = new ReminderAdapter(getContext(), reminderList);
        recyclerView.setAdapter(reminderAdapter);

        // Show empty message if no reminders
        updateEmptyView();

        // FAB click: open ReminderActivity
        fabAddReminder.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ReminderActivity.class));
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh list when returning
        reminderList = dbHelper.getAllReminders();
        reminderAdapter.updateList(reminderList);
        updateEmptyView();
    }

    private void updateEmptyView() {
        if (reminderList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
