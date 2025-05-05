package com.example.timesync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Receiver that handles device boot to reschedule all reminders.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // Here we would load all scheduled reminders from persistent storage
            // and reschedule them using ReminderManager
            
            // For demonstration, we'll reschedule a default reminder
            rescheduleReminders(context);
        }
    }
    
    /**
     * Reschedule all reminders from persistent storage.
     */
    private void rescheduleReminders(Context context) {
        // In a real app, you would load all scheduled reminders from a database
        // or SharedPreferences and reschedule them
        
        SharedPreferences prefs = context.getSharedPreferences("TimeSync_Reminders", Context.MODE_PRIVATE);
        
        // Check if there are any saved reminders
        if (prefs.contains("has_reminders") && prefs.getBoolean("has_reminders", false)) {
            ReminderManager reminderManager = ReminderManager.getInstance(context);
            
            // Example: Reschedule based on saved preferences
            // Here you would typically loop through all saved reminders
            int savedHour = prefs.getInt("reminder_hour", 9);
            int savedMinute = prefs.getInt("reminder_minute", 0);
            String savedTask = prefs.getString("reminder_task", "Your scheduled activity");
            
            // Reschedule the reminder
            reminderManager.scheduleReminderForTime(savedTask, savedHour, savedMinute);
        }
    }
} 