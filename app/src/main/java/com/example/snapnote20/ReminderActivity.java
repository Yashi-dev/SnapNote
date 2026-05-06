package com.example.snapnote20;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

// Activity to create a new reminder with action type
public class ReminderActivity extends AppCompatActivity {

    private TextInputEditText etReminderTitle, etPhone, etEmail, etWebsite;
    private TextView tvDate, tvTime;
    private MaterialButton btnPickDate, btnPickTime, btnSaveReminder;
    private RadioGroup radioGroupAction;
    private RadioButton rbCall, rbEmail, rbWebsite;
    private DatabaseHelper dbHelper;

    private String selectedDate = "";
    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        dbHelper = new DatabaseHelper(this);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Reminder");
        }

        // Find views
        etReminderTitle = findViewById(R.id.etReminderTitle);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etReminderEmail);
        etWebsite = findViewById(R.id.etWebsite);
        tvDate = findViewById(R.id.tvSelectedDate);
        tvTime = findViewById(R.id.tvSelectedTime);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnPickTime = findViewById(R.id.btnPickTime);
        btnSaveReminder = findViewById(R.id.btnSaveReminder);
        radioGroupAction = findViewById(R.id.radioGroupAction);
        rbCall = findViewById(R.id.rbCall);
        rbEmail = findViewById(R.id.rbEmail);
        rbWebsite = findViewById(R.id.rbWebsite);

        // Date picker
        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                tvDate.setText(selectedDate);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Time picker
        btnPickTime.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                tvTime.setText(selectedTime);
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        });

        // Save reminder
        btnSaveReminder.setOnClickListener(v -> {
            String title = etReminderTitle.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String website = etWebsite.getText().toString().trim();

            // Validate title
            if (TextUtils.isEmpty(title)) {
                etReminderTitle.setError("Title is required");
                return;
            }
            if (TextUtils.isEmpty(selectedDate)) {
                Snackbar.make(v, "Please pick a date", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(selectedTime)) {
                Snackbar.make(v, "Please pick a time", Snackbar.LENGTH_SHORT).show();
                return;
            }

            // Determine action type
            String actionType = "Call";
            int checkedId = radioGroupAction.getCheckedRadioButtonId();
            if (checkedId == R.id.rbEmail) actionType = "Email";
            else if (checkedId == R.id.rbWebsite) actionType = "Website";

            // Build and save reminder
            Reminder reminder = new Reminder();
            reminder.setNoteId(0);
            reminder.setTitle(title);
            reminder.setPhone(phone);
            reminder.setEmail(email);
            reminder.setWebsite(website);
            reminder.setDate(selectedDate);
            reminder.setTime(selectedTime);
            reminder.setActionType(actionType);

            dbHelper.addReminder(reminder); // Changed from insertReminder to addReminder
            Snackbar.make(v, "Reminder saved!", Snackbar.LENGTH_SHORT).show();

            new android.os.Handler().postDelayed(this::finish, 800);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}
