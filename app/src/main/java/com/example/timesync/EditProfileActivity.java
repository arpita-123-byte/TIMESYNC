package com.example.timesync;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

/**
 * Activity for editing user profile information.
 */
public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    private ImageView profileImage;
    private ImageView btnCamera;
    private ImageView btnBack;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editUsername;
    private EditText editEmail;
    private EditText editPhone;
    private EditText editGender;
    private Button btnChangePassword;
    private Button btnSaveProfile;
    
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        initViews();
        // Load profile data
        loadProfileData();
        // Set up click listeners
        setupListeners();
    }

    /**
     * Initialize all view references.
     */
    private void initViews() {
        profileImage = findViewById(R.id.profileImage);
        btnCamera = findViewById(R.id.btnCamera);
        btnBack = findViewById(R.id.btnBack);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editGender = findViewById(R.id.editGender);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
    }

    /**
     * Load profile data from preferences or intent extras.
     */
    private void loadProfileData() {
        // Check if there's data passed through intent extras
        Bundle extras = getIntent().getExtras();
        String email = null;
        
        if (extras != null) {
            String fullName = extras.getString("name", "");
            email = extras.getString("email", "");
            
            // Split full name into first and last name
            if (fullName != null && !fullName.isEmpty()) {
                String[] names = fullName.split(" ");
                if (names.length > 0) {
                    editFirstName.setText(names[0]);
                }
                if (names.length > 1) {
                    editLastName.setText(names[1]);
                }
            }
            
            if (email != null) {
                editEmail.setText(email);
            }
        } 
        
        // If email is set, try to get user data from UserManager
        if (email != null && !email.isEmpty()) {
            UserManager userManager = UserManager.getInstance(this);
            String name = userManager.getUserName(email);
            String phone = userManager.getUserPhone(email);
            
            // If we have user data from UserManager, use it
            if (!name.isEmpty()) {
                String[] names = name.split(" ");
                if (names.length > 0) {
                    editFirstName.setText(names[0]);
                }
                if (names.length > 1) {
                    editLastName.setText(names[1]);
                }
            }
            
            if (!phone.isEmpty()) {
                editPhone.setText(phone);
            }
        } else {
            // Try to get logged in user email from shared preferences
            String loggedInEmail = getSharedPreferences("TimeSync_Login", MODE_PRIVATE)
                    .getString("logged_in_user", "");
            
            if (!loggedInEmail.isEmpty()) {
                UserManager userManager = UserManager.getInstance(this);
                String name = userManager.getUserName(loggedInEmail);
                String phone = userManager.getUserPhone(loggedInEmail);
                
                editEmail.setText(loggedInEmail);
                
                if (!name.isEmpty()) {
                    String[] names = name.split(" ");
                    if (names.length > 0) {
                        editFirstName.setText(names[0]);
                    }
                    if (names.length > 1) {
                        editLastName.setText(names[1]);
                    }
                }
                
                if (!phone.isEmpty()) {
                    editPhone.setText(phone);
                }
            } else {
                // If no data is available, use empty fields instead of mock data
                editFirstName.setText("");
                editLastName.setText("");
                editUsername.setText("");
                editEmail.setText("");
                editPhone.setText("");
                editGender.setText("");
            }
        }
    }

    /**
     * Set up click listeners for buttons and interactive elements.
     */
    private void setupListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());
        
        btnCamera.setOnClickListener(v -> {
            // Show options to take photo or choose from gallery
            showImagePickerDialog();
        });
        
        btnChangePassword.setOnClickListener(v -> {
            // Navigate to set new password screen
            Intent intent = new Intent(EditProfileActivity.this, SetNewPasswordActivity.class);
            if (editEmail != null && !editEmail.getText().toString().trim().isEmpty()) {
                intent.putExtra("email", editEmail.getText().toString().trim());
            }
            startActivity(intent);
        });
        
        btnSaveProfile.setOnClickListener(v -> {
            // Save profile changes
            saveProfile();
        });
    }

    /**
     * Show dialog to choose between camera and gallery.
     */
    private void showImagePickerDialog() {
        // For simplicity, just open gallery directly
        openGallery();
        
        // In a real app, you would show a dialog with camera and gallery options
        // And handle the camera intent as well
    }

    /**
     * Open gallery to select image.
     */
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /**
     * Save profile information and return to previous screen.
     */
    private void saveProfile() {
        // Validate inputs
        if (validateInputs()) {
            String firstName = editFirstName.getText().toString().trim();
            String lastName = editLastName.getText().toString().trim();
            String fullName = firstName;
            if (!lastName.isEmpty()) {
                fullName += " " + lastName;
            }
            String email = editEmail.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            
            // Save to UserManager
            UserManager userManager = UserManager.getInstance(this);
            boolean success = userManager.updateUserProfile(email, fullName, phone);
            
            if (success) {
                // Update shared preferences if email has changed
                String currentLoggedInEmail = getSharedPreferences("TimeSync_Login", MODE_PRIVATE)
                        .getString("logged_in_user", "");
                
                if (!currentLoggedInEmail.equals(email)) {
                    getSharedPreferences("TimeSync_Login", MODE_PRIVATE)
                            .edit()
                            .putString("logged_in_user", email)
                            .apply();
                }
                
                // Create result intent with updated profile data for passing back to calling activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("firstName", firstName);
                resultIntent.putExtra("lastName", lastName);
                resultIntent.putExtra("username", editUsername.getText().toString().trim());
                resultIntent.putExtra("email", email);
                resultIntent.putExtra("phone", phone);
                resultIntent.putExtra("gender", editGender.getText().toString().trim());
                
                if (imageUri != null) {
                    resultIntent.putExtra("profileImage", imageUri.toString());
                }
                
                setResult(RESULT_OK, resultIntent);
                
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Validate all input fields.
     */
    private boolean validateInputs() {
        boolean isValid = true;
        
        // Check first name
        if (editFirstName.getText().toString().trim().isEmpty()) {
            editFirstName.setError("First name cannot be empty");
            isValid = false;
        }
        
        // Check email
        String email = editEmail.getText().toString().trim();
        if (email.isEmpty()) {
            editEmail.setError("Email cannot be empty");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please enter a valid email");
            isValid = false;
        }
        
        return isValid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
} 