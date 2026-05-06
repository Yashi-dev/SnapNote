package com.example.snapnote20;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

// Adapter class for Reminders RecyclerView
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private Context context;
    private List<Reminder> reminderList;
    private DatabaseHelper dbHelper;

    public ReminderAdapter(Context context, List<Reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_reminder_adapter, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);

        // Set reminder data
        holder.tvTitle.setText(reminder.getTitle());
        holder.tvDate.setText("Date: " + reminder.getDate());
        holder.tvTime.setText("Time: " + reminder.getTime());
        holder.tvActionType.setText("Action: " + reminder.getActionType());

        // Action button click: perform intent based on type
        holder.btnAction.setOnClickListener(v -> {
            performAction(reminder);
        });

        // Delete button click
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Reminder")
                    .setMessage("Are you sure you want to delete this reminder?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        dbHelper.deleteReminder(reminder.getId());
                        reminderList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, reminderList.size());
                        Snackbar.make(v, "Reminder deleted", Snackbar.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    // Perform implicit intent based on action type
    private void performAction(Reminder reminder) {
        String actionType = reminder.getActionType();
        Intent intent = null;

        if ("Call".equalsIgnoreCase(actionType)) {
            // Open dialer with phone number
            intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + reminder.getPhone()));

        } else if ("Email".equalsIgnoreCase(actionType)) {
            // Open email app
            intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + reminder.getEmail()));
            intent.putExtra(Intent.EXTRA_SUBJECT, reminder.getTitle());

        } else if ("Website".equalsIgnoreCase(actionType)) {
            // Open browser
            String url = reminder.getWebsite();
            if (url != null) {
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://" + url;
                }
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
            }
        }

        if (intent != null) {
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    // Update list
    public void updateList(List<Reminder> newList) {
        reminderList = newList;
        notifyDataSetChanged();
    }

    // ViewHolder
    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvTime, tvActionType;
        MaterialButton btnAction;
        ImageButton btnDelete;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvReminderTitle);
            tvDate = itemView.findViewById(R.id.tvReminderDate);
            tvTime = itemView.findViewById(R.id.tvReminderTime);
            tvActionType = itemView.findViewById(R.id.tvReminderAction);
            btnAction = itemView.findViewById(R.id.btnPerformAction);
            btnDelete = itemView.findViewById(R.id.btnDeleteReminder);
        }
    }
}
