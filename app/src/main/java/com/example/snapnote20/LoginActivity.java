package com.example.snapnote20;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

// Login screen
public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegister;
    private SharedPrefHelper prefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefHelper = new SharedPrefHelper(this);

        // Find views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Login button click
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validate fields
            if (TextUtils.isEmpty(username)) {
                etUsername.setError("Enter username or email");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Enter password");
                return;
            }

            // Simple validation: check if username matches saved username
            String savedUsername = prefHelper.getUsername();
            String savedEmail = prefHelper.getEmail();

            if (username.equals(savedUsername) || username.equals(savedEmail)) {
                // Login successful
                prefHelper.setLoggedIn(true);
                Snackbar.make(v, "Login successful!", Snackbar.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else {
                Snackbar.make(v, "Invalid credentials. Please register first.", Snackbar.LENGTH_LONG).show();
            }
        });

        // Go to RegisterActivity
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}