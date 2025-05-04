package com.example.timesync.model;

public class ScheduleItem {
    private String startTime;
    private String endTime;
    private String title;
    private String subtitle;
    private String teacherName;
    private String platform;
    private int backgroundColor;
    private int teacherImageResourceId;
    private int platformImageResourceId;
    private boolean isAssignment;

    public ScheduleItem(String startTime, String endTime, String title, String subtitle, 
                      String teacherName, String platform, int backgroundColor,
                      int teacherImageResourceId, int platformImageResourceId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.subtitle = subtitle;
        this.teacherName = teacherName;
        this.platform = platform;
        this.backgroundColor = backgroundColor;
        this.teacherImageResourceId = teacherImageResourceId;
        this.platformImageResourceId = platformImageResourceId;
        this.isAssignment = false;
    }

    public ScheduleItem(String startTime, String endTime, String title, String subtitle, 
                      String teacherName, String platform, int backgroundColor,
                      int teacherImageResourceId, int platformImageResourceId, boolean isAssignment) {
        this(startTime, endTime, title, subtitle, teacherName, platform, backgroundColor,
             teacherImageResourceId, platformImageResourceId);
        this.isAssignment = isAssignment;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getPlatform() {
        return platform;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTeacherImageResourceId() {
        return teacherImageResourceId;
    }

    public int getPlatformImageResourceId() {
        return platformImageResourceId;
    }
    
    public boolean isAssignment() {
        return isAssignment;
    }
} 