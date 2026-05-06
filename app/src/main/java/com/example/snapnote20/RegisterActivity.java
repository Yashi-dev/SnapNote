package com.example.snapnote20;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

// Registration screen
public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEmail, etUsername, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private SharedPrefHelper prefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        prefHelper = new SharedPrefHelper(this);

        // Find views
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Register button click
        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Validate all fields
            if (TextUtils.isEmpty(fullName)) { etFullName.setError("Enter full name"); return; }
            if (TextUtils.isEmpty(email)) { etEmail.setError("Enter email"); return; }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Enter valid email"); return;
            }
            if (TextUtils.isEmpty(username)) { etUsername.setError("Enter username"); return; }
            if (TextUtils.isEmpty(password)) { etPassword.setError("Enter password"); return; }
            if (password.length() < 4) { etPassword.setError("Min 4 characters"); return; }
            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError("Passwords do not match"); return;
            }

            // Save user details in SharedPreferences
            prefHelper.setFullName(fullName);
            prefHelper.setEmail(email);
            prefHelper.setUsername(username);

            Snackbar.make(v, "Registered successfully! Please login.", Snackbar.LENGTH_LONG).show();

            // Go back to login
            new android.os.Handler().postDelayed(() -> {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }, 1500);
        });
    }
}