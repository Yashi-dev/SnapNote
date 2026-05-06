package com.example.snapnote20;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextInputEditText etFullName, etEmail;
    private ImageView ivProfileImage;
    private FloatingActionButton fabEditPhoto;
    private MaterialButton btnUpdate, btnLogout;
    private com.google.android.material.textview.MaterialTextView tvNotesCount, tvRemindersCount;
    private androidx.appcompat.widget.SwitchCompat switchDarkMode;

    private SharedPrefHelper prefHelper;
    private DatabaseHelper dbHelper;
    private Uri selectedImageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_fragment, container, false);

        prefHelper = new SharedPrefHelper(getContext());
        dbHelper = new DatabaseHelper(getContext());

        // Initialize views
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        fabEditPhoto = view.findViewById(R.id.fabEditPhoto);
        etFullName = view.findViewById(R.id.etProfileFullName);
        etEmail = view.findViewById(R.id.etProfileEmail);
        btnUpdate = view.findViewById(R.id.btnUpdateProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        tvNotesCount = view.findViewById(R.id.tvNotesCount);
        tvRemindersCount = view.findViewById(R.id.tvRemindersCount);
        switchDarkMode = view.findViewById(R.id.switchDarkMode);

        // Load existing data
        loadUserData();

        // Stats
        tvNotesCount.setText(String.valueOf(dbHelper.getNotesCount()));
        tvRemindersCount.setText(String.valueOf(dbHelper.getReminderCount()));

        // Photo pick
        fabEditPhoto.setOnClickListener(v -> openGallery());

        // Update profile
        btnUpdate.setOnClickListener(v -> updateProfile());

        // Dark mode
        switchDarkMode.setChecked(prefHelper.isDarkMode());
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefHelper.setDarkMode(isChecked);
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            prefHelper.logout();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    private void loadUserData() {
        etFullName.setText(prefHelper.getFullName());
        etEmail.setText(prefHelper.getEmail());
        
        String imagePath = prefHelper.getProfileImage();
        if (!TextUtils.isEmpty(imagePath)) {
            ivProfileImage.setImageURI(Uri.parse(imagePath));
            ivProfileImage.setPadding(0, 0, 0, 0); // Remove padding if image is set
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            ivProfileImage.setImageURI(selectedImageUri);
            ivProfileImage.setPadding(0, 0, 0, 0);
        }
    }

    private void updateProfile() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Name required");
            return;
        }

        prefHelper.setFullName(fullName);
        prefHelper.setEmail(email);
        
        if (selectedImageUri != null) {
            prefHelper.setProfileImage(selectedImageUri.toString());
        }

        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    }
}
