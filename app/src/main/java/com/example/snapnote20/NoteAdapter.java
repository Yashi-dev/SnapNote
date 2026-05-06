package com.example.snapnote20;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;
    private List<Note> noteList;
    private DatabaseHelper dbHelper;

    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);

        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());
        holder.tvCategory.setText(note.getCategory());
        holder.tvDate.setText(note.getCreatedAt());

        // Handle Pin Visibility
        holder.ivPinned.setVisibility(note.getIsPinned() == 1 ? View.VISIBLE : View.GONE);

        // Handle Priority Indicator Color (Dark Pastel Palette)
        int priorityColor;
        if (note.getPriority() != null) {
            switch (note.getPriority().toLowerCase()) {
                case "high":
                    priorityColor = ContextCompat.getColor(context, R.color.priorityHigh);
                    break;
                case "medium":
                    priorityColor = ContextCompat.getColor(context, R.color.priorityMedium);
                    break;
                default:
                    priorityColor = ContextCompat.getColor(context, R.color.priorityLow);
                    break;
            }
        } else {
            priorityColor = ContextCompat.getColor(context, R.color.priorityLow);
        }
        holder.viewPriorityIndicator.setBackgroundTintList(ColorStateList.valueOf(priorityColor));

        // Handle Category Tag Color (Muted/Darker Palette)
        int tagColor;
        if (note.getCategory() != null) {
            switch (note.getCategory().toLowerCase()) {
                case "study":
                    tagColor = ContextCompat.getColor(context, R.color.tagStudy);
                    break;
                case "personal":
                    tagColor = ContextCompat.getColor(context, R.color.tagPersonal);
                    break;
                case "work":
                    tagColor = ContextCompat.getColor(context, R.color.tagWork);
                    break;
                case "important":
                    tagColor = ContextCompat.getColor(context, R.color.tagImportant);
                    break;
                default:
                    tagColor = ContextCompat.getColor(context, R.color.colorElevated);
                    break;
            }
        } else {
            tagColor = ContextCompat.getColor(context, R.color.colorElevated);
        }
        holder.tvCategory.setBackgroundTintList(ColorStateList.valueOf(tagColor));
        holder.tvCategory.setTextColor(ContextCompat.getColor(context, R.color.colorText));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NoteDetailActivity.class);
            intent.putExtra("note_id", note.getId());
            intent.putExtra("note_title", note.getTitle());
            intent.putExtra("note_content", note.getContent());
            intent.putExtra("note_category", note.getCategory());
            intent.putExtra("note_priority", note.getPriority());
            intent.putExtra("note_date", note.getCreatedAt());
            intent.putExtra("note_image", note.getImagePath());
            intent.putExtra("note_pinned", note.getIsPinned());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            showOptionsDialog(note, position, v);
            return true;
        });
    }

    private void showOptionsDialog(Note note, int position, View view) {
        String pinLabel = note.getIsPinned() == 1 ? "Unpin Note" : "Pin Note";
        String[] options = {"Edit", "Delete", pinLabel};

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_SnapNote);
        builder.setTitle("Note Options");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                Intent intent = new Intent(context, AddNoteActivity.class);
                intent.putExtra("edit_mode", true);
                intent.putExtra("note_id", note.getId());
                intent.putExtra("note_title", note.getTitle());
                intent.putExtra("note_content", note.getContent());
                intent.putExtra("note_category", note.getCategory());
                intent.putExtra("note_priority", note.getPriority());
                intent.putExtra("note_image", note.getImagePath());
                context.startActivity(intent);
            } else if (which == 1) {
                dbHelper.deleteNote(note.getId());
                noteList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, noteList.size());
                Snackbar.make(view, "Note deleted", Snackbar.LENGTH_SHORT).show();
            } else if (which == 2) {
                int newPinStatus = note.getIsPinned() == 1 ? 0 : 1;
                note.setIsPinned(newPinStatus);
                dbHelper.updateNote(note);
                notifyItemChanged(position);
                String msg = newPinStatus == 1 ? "Note pinned" : "Note unpinned";
                Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void updateList(List<Note> newList) {
        this.noteList = newList;
        notifyDataSetChanged();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvCategory, tvDate;
        ImageView ivPinned;
        View viewPriorityIndicator;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvContent = itemView.findViewById(R.id.tvNoteContent);
            tvCategory = itemView.findViewById(R.id.tvNoteCategory);
            tvDate = itemView.findViewById(R.id.tvNoteDate);
            ivPinned = itemView.findViewById(R.id.ivPinned);
            viewPriorityIndicator = itemView.findViewById(R.id.viewPriorityIndicator);
        }
    }
}
