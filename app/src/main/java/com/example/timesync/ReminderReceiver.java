package com.example.timesync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;

/**
 * Receiver for handling scheduled reminders.
 */
public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Acquire a wake lock to ensure the device stays awake long enough to handle the notification
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "TimeSync:ReminderWakeLock"
        );
        
        wakeLock.acquire(10 * 60 * 1000L); // 10 minutes
        
        try {
            String taskTitle = intent.getStringExtra("taskTitle");
            if (taskTitle == null) {
                taskTitle = "Your scheduled activity";
            }
            
            // Show a notification for the reminder
            ReminderManager.getInstance(context).showNotification(taskTitle);
            
            // Start an activity to show a dialog over other apps if permission is granted
            if (canDrawOverlays(context)) {
                Intent dialogIntent = new Intent(context, ReminderDialogActivity.class);
                dialogIntent.putExtra("taskTitle", taskTitle);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(dialogIntent);
            }
        } finally {
            wakeLock.release();
        }
    }
    
    /**
     * Check if the app has permission to draw over other apps.
     */
    private boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // For Android 6.0+, check if the app has permission to draw over other apps
            return android.provider.Settings.canDrawOverlays(context);
        }
        return true; // For older versions, no specific permission is needed
    }
} 