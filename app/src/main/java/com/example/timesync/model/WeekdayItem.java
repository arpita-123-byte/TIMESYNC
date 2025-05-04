package com.example.timesync.model;

public class WeekdayItem {
    private String dayName;
    private String dayNumber;
    private boolean isSelected;

    public WeekdayItem(String dayName, String dayNumber, boolean isSelected) {
        this.dayName = dayName;
        this.dayNumber = dayNumber;
        this.isSelected = isSelected;
    }

    public String getDayName() {
        return dayName;
    }

    public String getDayNumber() {
        return dayNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
} 