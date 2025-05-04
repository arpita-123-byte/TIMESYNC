package com.example.timesync;

import java.util.Calendar;

/**
 * Model class to represent time statistics for a specific date
 */
public class TimeStats {
    private Calendar date;
    private long totalTimeMillis;
    private long productiveTimeMillis;
    private long unproductiveTimeMillis;
    private float percentChange;

    public TimeStats(Calendar date, long totalTimeMillis, long productiveTimeMillis, 
                    long unproductiveTimeMillis, float percentChange) {
        this.date = date;
        this.totalTimeMillis = totalTimeMillis;
        this.productiveTimeMillis = productiveTimeMillis;
        this.unproductiveTimeMillis = unproductiveTimeMillis;
        this.percentChange = percentChange;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public long getTotalTimeMillis() {
        return totalTimeMillis;
    }

    public void setTotalTimeMillis(long totalTimeMillis) {
        this.totalTimeMillis = totalTimeMillis;
    }

    public long getProductiveTimeMillis() {
        return productiveTimeMillis;
    }

    public void setProductiveTimeMillis(long productiveTimeMillis) {
        this.productiveTimeMillis = productiveTimeMillis;
    }

    public long getUnproductiveTimeMillis() {
        return unproductiveTimeMillis;
    }

    public void setUnproductiveTimeMillis(long unproductiveTimeMillis) {
        this.unproductiveTimeMillis = unproductiveTimeMillis;
    }

    public float getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(float percentChange) {
        this.percentChange = percentChange;
    }

    /**
     * Format time in hours and minutes (e.g., "09h 41m")
     */
    public static String formatTime(long timeMillis) {
        long hours = timeMillis / (60 * 60 * 1000);
        long minutes = (timeMillis % (60 * 60 * 1000)) / (60 * 1000);
        return String.format("%02dh %02dm", hours, minutes);
    }

    /**
     * Format percent change with arrow (e.g., "↑ 4.5%" or "↓ 2.3%")
     */
    public static String formatPercentChange(float percentChange) {
        String arrow = percentChange >= 0 ? "↑" : "↓";
        return String.format("%s %.1f%%", arrow, Math.abs(percentChange));
    }
} 