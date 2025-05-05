package com.example.timesync;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * ContactUsActivity provides various contact options to the user
 * including call, email, and social media channels.
 */
public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        
        // Set status bar color
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));
        
        // Make status bar icons dark for better visibility on white background
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setupBackButton();
        setupContactOptions();
        setupSocialMediaOptions();
    }

    /**
     * Set up the back button to navigate to the previous screen.
     */
    private void setupBackButton() {
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    /**
     * Set up call and email contact options.
     */
    private void setupContactOptions() {
        LinearLayout callUsOption = findViewById(R.id.callUsOption);
        LinearLayout emailUsOption = findViewById(R.id.emailUsOption);

        // Call option
        callUsOption.setOnClickListener(v -> {
            try {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+123456789"));
                startActivity(callIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Could not initiate call", Toast.LENGTH_SHORT).show();
            }
        });

        // Email option
        emailUsOption.setOnClickListener(v -> {
            try {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:support@timesync.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact from App");
                startActivity(emailIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Could not open email app", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Set up social media contact options.
     */
    private void setupSocialMediaOptions() {
        LinearLayout instagramOption = findViewById(R.id.instagramOption);
        LinearLayout telegramOption = findViewById(R.id.telegramOption);
        LinearLayout facebookOption = findViewById(R.id.facebookOption);
        LinearLayout whatsappOption = findViewById(R.id.whatsappOption);

        // Instagram
        instagramOption.setOnClickListener(v -> {
            openSocialMedia("https://instagram.com/timesync");
        });

        // Telegram
        telegramOption.setOnClickListener(v -> {
            openSocialMedia("https://t.me/timesync");
        });

        // Facebook
        facebookOption.setOnClickListener(v -> {
            openSocialMedia("https://facebook.com/timesync");
        });

        // WhatsApp
        whatsappOption.setOnClickListener(v -> {
            try {
                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                String url = "https://api.whatsapp.com/send?phone=+123456789";
                whatsappIntent.setData(Uri.parse(url));
                startActivity(whatsappIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Could not open WhatsApp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Open a social media link in the browser.
     *
     * @param url The URL to open
     */
    private void openSocialMedia(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Could not open link", Toast.LENGTH_SHORT).show();
        }
    }
} 