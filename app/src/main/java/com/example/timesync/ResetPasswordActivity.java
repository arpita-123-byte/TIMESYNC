package com.example.timesync;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private Button btnUpdatePassword;
    
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Get the email from the previous screen
        email = getIntent().getStringExtra("email");
        if (email == null) {
            email = "example@gmail.com";
        }

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);

        // Set click listeners
        btnBack.setOnClickListener(v -> onBackPressed());

        btnUpdatePassword.setOnClickListener(v -> {
            if (validatePasswordInputs()) {
                // In a real app, this would update the password in a database or server
                // For now, just show a success message and navigate back to the sign-in screen
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                
                // Navigate to sign in screen
                Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Validate the password inputs
     */
    private boolean validatePasswordInputs() {
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        // Validate password
        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Password is required");
            return false;
        }

        if (password.length() < 6) {
            editPassword.setError("Password must be at least 6 characters");
            return false;
        }

        // Validate confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            editConfirmPassword.setError("Please confirm your password");
            return false;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            editConfirmPassword.setError("Passwords do not match");
            return false;
        }

        return true;
    }
} 