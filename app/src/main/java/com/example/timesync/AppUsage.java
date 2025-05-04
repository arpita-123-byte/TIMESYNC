package com.example.timesync;

public class AppUsage {
    public String appName, usageTime;
    public int iconRes, productivity;

    public AppUsage(String appName, String usageTime, int iconRes, int productivity) {
        this.appName = appName;
        this.usageTime = usageTime;
        this.iconRes = iconRes;
        this.productivity = productivity;
    }
} 