package com.example.timesync;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarView extends GridLayout {
    private static final int DAYS_IN_WEEK = 7;
    private static final int MAX_WEEKS = 6;
    private static final String[] DAYS_OF_WEEK = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    private Calendar currentCalendar;
    private Calendar selectedDate = null; // Track selected date
    private OnDateSelectedListener dateSelectedListener;
    private List<TextView> dayViews = new ArrayList<>(); // Keep references to day views

    public interface OnDateSelectedListener {
        void onDateSelected(Calendar date);
    }

    public CalendarView(Context context) {
        super(context);
        init();
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setColumnCount(DAYS_IN_WEEK);
        setRowCount(MAX_WEEKS + 1); // +1 for the header row

        currentCalendar = Calendar.getInstance();
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1); // Start with the 1st day of month

        // Add day of week headers
        addDayHeaders();
        
        // Add calendar days
        populateCalendar();
    }

    private void addDayHeaders() {
        for (String dayName : DAYS_OF_WEEK) {
            TextView textView = new TextView(getContext());
            textView.setText(dayName);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(4, 8, 4, 8);
            textView.setTextSize(12);
            textView.setTextColor(Color.GRAY);
            
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setGravity(Gravity.FILL_HORIZONTAL);
            
            addView(textView, params);
        }
    }

    private void populateCalendar() {
        dayViews.clear(); // Clear previous day views
        Calendar calendar = (Calendar) currentCalendar.clone();
        
        // Determine the day of week for the 1st of the month
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        
        // Go back to the first day of the grid (may be in previous month)
        calendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);
        
        // Create cells for each day
        for (int i = 0; i < DAYS_IN_WEEK * MAX_WEEKS; i++) {
            final TextView dayView = new TextView(getContext());
            final Calendar cellDate = (Calendar) calendar.clone();
            
            dayView.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            dayView.setGravity(Gravity.CENTER);
            dayView.setPadding(8, 16, 8, 16);
            dayView.setTextSize(16); // Larger text size to match screenshot
            
            // Check if the day is in the current month
            boolean isCurrentMonth = calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH);
            dayView.setTextColor(isCurrentMonth ? Color.BLACK : Color.LTGRAY);
            
            // Check if it's the current day
            Calendar today = Calendar.getInstance();
            boolean isToday = calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                             calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                             calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
            
            // Check if it's the selected date
            boolean isSelected = isDateSelected(cellDate);
            
            // Set appropriate background
            if (isSelected) {
                dayView.setBackgroundResource(R.drawable.blue_selection_bg);
                dayView.setTextColor(Color.WHITE);
            } else if (isToday) {
                dayView.setBackgroundResource(R.drawable.blue_circle);
                dayView.setTextColor(Color.WHITE);
            } else {
                dayView.setBackground(null);
            }
            
            // Add click listener
            dayView.setOnClickListener(v -> {
                selectDate(cellDate, dayView);
                if (dateSelectedListener != null) {
                    dateSelectedListener.onDateSelected(cellDate);
                }
            });
            
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setGravity(Gravity.FILL_HORIZONTAL | Gravity.CENTER);
            
            // Add margins around day views
            params.setMargins(4, 8, 4, 8);
            
            addView(dayView, params);
            dayViews.add(dayView); // Store reference to the day view
            
            // Move to the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private boolean isDateSelected(Calendar date) {
        if (selectedDate == null) {
            return false;
        }
        return date.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
               date.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
               date.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH);
    }
    
    private void selectDate(Calendar date, TextView selectedDayView) {
        // Reset previous selection
        resetSelections();
        
        // Update selected date
        selectedDate = date;
        
        // Update UI
        selectedDayView.setBackgroundResource(R.drawable.blue_selection_bg);
        selectedDayView.setTextColor(Color.WHITE);
    }
    
    private void resetSelections() {
        Calendar today = Calendar.getInstance();
        
        for (int i = 0; i < dayViews.size(); i++) {
            TextView dayView = dayViews.get(i);
            
            // Calculate this cell's date
            Calendar cellDate = (Calendar) currentCalendar.clone();
            cellDate.set(Calendar.DAY_OF_MONTH, 1);
            int firstDayOfMonth = cellDate.get(Calendar.DAY_OF_WEEK) - 1;
            cellDate.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth + i);
            
            // Check if it's today
            boolean isToday = cellDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                             cellDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                             cellDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
            
            // Check if it's in the current month
            boolean isCurrentMonth = cellDate.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH);
            
            // Reset background and text color
            if (isToday) {
                dayView.setBackgroundResource(R.drawable.blue_circle);
                dayView.setTextColor(Color.WHITE);
            } else {
                dayView.setBackground(null);
                dayView.setTextColor(isCurrentMonth ? Color.BLACK : Color.LTGRAY);
            }
        }
    }

    public void setMonth(int year, int month) {
        removeAllViews();
        
        currentCalendar.set(Calendar.YEAR, year);
        currentCalendar.set(Calendar.MONTH, month);
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
        
        addDayHeaders();
        populateCalendar();
    }
    
    public void nextMonth() {
        currentCalendar.add(Calendar.MONTH, 1);
        setMonth(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH));
    }
    
    public void previousMonth() {
        currentCalendar.add(Calendar.MONTH, -1);
        setMonth(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH));
    }
    
    public String getCurrentMonthName() {
        return currentCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }
    
    public int getCurrentYear() {
        return currentCalendar.get(Calendar.YEAR);
    }
    
    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.dateSelectedListener = listener;
    }
    
    public void setSelectedDate(Calendar date) {
        selectedDate = date;
        // Refresh the calendar view
        setMonth(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH));
    }
} 