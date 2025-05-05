package com.example.timesync;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Manages reminders for tasks and activities in the timetable.
 */
public class ReminderManager {

    private static final String CHANNEL_ID = "TimeSync_Reminders";
    private static final int NOTIFICATION_ID = 100;
    private static final List<String> motivationalQuotes = new ArrayList<>();

    // Singleton instance
    private static ReminderManager instance;
    private Context context;

    private ReminderManager(Context context) {
        this.context = context.getApplicationContext();
        initializeQuotes();
        createNotificationChannel();
    }

    /**
     * Get the ReminderManager instance.
     */
    public static synchronized ReminderManager getInstance(Context context) {
        if (instance == null) {
            instance = new ReminderManager(context);
        }
        return instance;
    }

    /**
     * Initialize the list of motivational quotes.
     */
    private void initializeQuotes() {
        motivationalQuotes.add("Small efforts every day lead to big achievements. Keep going!");
        motivationalQuotes.add("Your future is created by what you do today, not tomorrow.");
        motivationalQuotes.add("Focus on your goal. Don't look in any direction but ahead.");
        motivationalQuotes.add("Success is the sum of small efforts repeated day in and day out.");
        motivationalQuotes.add("The journey of a thousand miles begins with a single step.");
        motivationalQuotes.add("Don't wait for opportunity, create it.");
        motivationalQuotes.add("Every accomplishment starts with the decision to try.");
        motivationalQuotes.add("The harder you work for something, the greater you'll feel when you achieve it.");
    }

    /**
     * Create a notification channel for Android 8.0 and above.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TimeSync Reminders";
            String description = "Reminders for scheduled activities";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Schedule a reminder for a task.
     *
     * @param taskTitle The title of the task
     * @param timeInMillis The time to show the reminder
     */
    public void scheduleReminder(String taskTitle, long timeInMillis) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("taskTitle", taskTitle);
        
        // Use unique request code based on time to avoid overriding
        int requestCode = (int) (timeInMillis % Integer.MAX_VALUE);
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // For Android 12+, check if we can schedule exact alarms
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
                } else {
                    // Fall back to inexact alarm
                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
                    Toast.makeText(context, "Please allow exact alarms for precise reminders", Toast.LENGTH_LONG).show();
                    
                    // Redirect to settings if user is in an activity
                    if (context instanceof Activity) {
                        Intent settingsIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                        context.startActivity(settingsIntent);
                    }
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            }
            
            // Log the scheduled time
            Log.d("ReminderManager", "Scheduled reminder for: " + new Date(timeInMillis).toString());
            
        } catch (Exception e) {
            Log.e("ReminderManager", "Error scheduling reminder: " + e.getMessage());
            Toast.makeText(context, "Could not schedule reminder: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Cancel a scheduled reminder.
     *
     * @param timeInMillis The time of the reminder to cancel
     */
    public void cancelReminder(long timeInMillis) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        int requestCode = (int) (timeInMillis % Integer.MAX_VALUE);
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_NO_CREATE
        );
        
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    /**
     * Show a notification for a task.
     *
     * @param taskTitle The title of the task
     */
    public void showNotification(String taskTitle) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        String quote = getRandomQuote();
        
        // Use app icon as fallback if custom notification icon is not available
        int iconResource;
        try {
            iconResource = R.drawable.ic_notification;
            context.getResources().getDrawable(iconResource);
        } catch (Exception e) {
            // Fallback to app icon
            iconResource = context.getApplicationInfo().icon;
        }
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(iconResource)
                .setContentTitle("Reminder: " + taskTitle)
                .setContentText(quote)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(quote))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * Show a dialog reminder.
     *
     * @param context The activity context
     * @param taskTitle The title of the task
     * @param onGetItClicked Callback when "GET IT" is clicked
     */
    public void showReminderDialog(Context context, String taskTitle, Runnable onGetItClicked) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reminder);
        dialog.setCancelable(false);
        
        TextView messageTextView = dialog.findViewById(R.id.reminderMessage);
        TextView taskTextView = dialog.findViewById(R.id.taskTitle);
        Button getItButton = dialog.findViewById(R.id.btnGetIt);
        Button closeButton = dialog.findViewById(R.id.btnClose);
        
        messageTextView.setText("\"" + getRandomQuote() + "\"");
        taskTextView.setText(taskTitle + " 💪🏆");
        
        getItButton.setOnClickListener(v -> {
            if (onGetItClicked != null) {
                onGetItClicked.run();
            }
            dialog.dismiss();
        });
        
        closeButton.setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
    }

    /**
     * Get a random motivational quote.
     */
    private String getRandomQuote() {
        Random random = new Random();
        return motivationalQuotes.get(random.nextInt(motivationalQuotes.size()));
    }

    /**
     * Schedule a reminder for a specific time.
     *
     * @param taskTitle The title of the task
     * @param hour The hour of the day (24-hour format)
     * @param minute The minute of the hour
     */
    public void scheduleReminderForTime(String taskTitle, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        
        // If the time is in the past, schedule for tomorrow
        if (calendar.before(now)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        scheduleReminder(taskTitle, calendar.getTimeInMillis());
    }
} 