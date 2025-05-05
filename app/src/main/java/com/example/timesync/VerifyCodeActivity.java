package com.example.timesync;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VerifyCodeActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView txtDescription;
    private TextView txtResendCode;
    private Button btnVerifyCode;
    
    private EditText digitOne, digitTwo, digitThree, digitFour, digitFive, digitSix;
    private EditText[] digitFields;
    
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        // Get the email from the previous screen
        email = getIntent().getStringExtra("email");
        if (email == null) {
            email = "example@gmail.com";
        }

        // Initialize views
        initializeViews();
        
        // Set up description text with the email
        txtDescription.setText("We sent a code to " + email + "\nenter 6 digit code that mentioned in the email");

        // Set click listeners
        setupClickListeners();
        
        // Set up digit fields to auto-advance
        setupDigitFields();
    }

    /**
     * Initialize all views
     */
    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        txtDescription = findViewById(R.id.txtDescription);
        txtResendCode = findViewById(R.id.txtResendCode);
        btnVerifyCode = findViewById(R.id.btnVerifyCode);
        
        digitOne = findViewById(R.id.digitOne);
        digitTwo = findViewById(R.id.digitTwo);
        digitThree = findViewById(R.id.digitThree);
        digitFour = findViewById(R.id.digitFour);
        digitFive = findViewById(R.id.digitFive);
        digitSix = findViewById(R.id.digitSix);
        
        digitFields = new EditText[]{digitOne, digitTwo, digitThree, digitFour, digitFive, digitSix};
    }

    /**
     * Set up click listeners for buttons
     */
    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());
        
        btnVerifyCode.setOnClickListener(v -> {
            if (validateCode()) {
                // In a real app, we would verify the code with a server
                // For now, just navigate to the reset password screen
                Intent intent = new Intent(VerifyCodeActivity.this, ResetPasswordActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
        
        txtResendCode.setOnClickListener(v -> {
            // In a real app, this would resend the verification code
            Toast.makeText(VerifyCodeActivity.this, "Verification code resent", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Set up digit fields to auto-advance to the next field
     */
    private void setupDigitFields() {
        for (int i = 0; i < digitFields.length; i++) {
            final int currentIndex = i;
            
            digitFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && currentIndex < digitFields.length - 1) {
                        // Auto-advance to next field
                        digitFields[currentIndex + 1].requestFocus();
                    }
                }
            });
            
            // Set listener to handle backspace
            digitFields[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && currentIndex > 0 && 
                        digitFields[currentIndex].getText().toString().isEmpty()) {
                    // If current field is empty and backspace is pressed, go back to previous field
                    digitFields[currentIndex - 1].requestFocus();
                    return true;
                }
                return false;
            });
        }
    }

    /**
     * Validate the verification code
     */
    private boolean validateCode() {
        StringBuilder codeBuilder = new StringBuilder();
        boolean isValid = true;
        
        for (EditText digitField : digitFields) {
            String digit = digitField.getText().toString();
            
            if (digit.isEmpty()) {
                digitField.setError("");
                isValid = false;
            }
            
            codeBuilder.append(digit);
        }
        
        if (!isValid) {
            Toast.makeText(this, "Please enter all digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        // For demo purposes, accept any 6-digit code
        return codeBuilder.length() == 6;
    }
} 