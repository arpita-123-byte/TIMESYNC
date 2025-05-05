package com.example.timesync;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText editEmail;
    private Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        editEmail = findViewById(R.id.editEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        // Set click listeners
        btnBack.setOnClickListener(v -> onBackPressed());

        btnResetPassword.setOnClickListener(v -> {
            if (validateEmail()) {
                // In a real app, this would send a reset code to the email
                // For now, just navigate to the verification screen
                Intent intent = new Intent(ForgotPasswordActivity.this, VerifyCodeActivity.class);
                intent.putExtra("email", editEmail.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

    /**
     * Validate the email input
     */
    private boolean validateEmail() {
        String email = editEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Email is required");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please enter a valid email");
            return false;
        }

        return true;
    }
} 