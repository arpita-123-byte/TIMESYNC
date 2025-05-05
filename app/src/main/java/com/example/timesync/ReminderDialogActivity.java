package com.example.timesync;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity that displays a reminder dialog.
 * This activity has a transparent background and shows just the reminder dialog.
 */
public class ReminderDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set up the window to appear on top of the lock screen
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
        
        setContentView(R.layout.activity_reminder_dialog);
        
        // Get the task title from the intent
        String taskTitle = getIntent().getStringExtra("taskTitle");
        if (taskTitle == null) {
            taskTitle = "Your scheduled activity";
        }
        
        // Set up the reminder dialog
        TextView messageTextView = findViewById(R.id.reminderMessage);
        TextView taskTextView = findViewById(R.id.taskTitle);
        Button getItButton = findViewById(R.id.btnGetIt);
        Button closeButton = findViewById(R.id.btnClose);
        
        // Set a random quote and the task title
        messageTextView.setText("\"" + getRandomQuote() + "\"");
        taskTextView.setText(taskTitle + " 💪🏆");
        
        // Set up button click listeners
        getItButton.setOnClickListener(v -> {
            // Here you could implement additional logic to mark the task as started
            finish();
        });
        
        closeButton.setOnClickListener(v -> finish());
    }
    
    /**
     * Get a random motivational quote.
     */
    private String getRandomQuote() {
        String[] quotes = {
            "Small efforts every day lead to big achievements. Keep going!",
            "Your future is created by what you do today, not tomorrow.",
            "Focus on your goal. Don't look in any direction but ahead.",
            "Success is the sum of small efforts repeated day in and day out.",
            "The journey of a thousand miles begins with a single step.",
            "Don't wait for opportunity, create it.",
            "Every accomplishment starts with the decision to try.",
            "The harder you work for something, the greater you'll feel when you achieve it."
        };
        
        return quotes[(int) (Math.random() * quotes.length)];
    }
} 