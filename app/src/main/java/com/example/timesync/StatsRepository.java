package com.example.timesync;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository class to fetch statistics data.
 * Note: This is a mock implementation for demonstration purposes.
 * In a real app, this would fetch data from a local database or a remote API.
 */
public class StatsRepository {
    private static StatsRepository instance;
    private Map<String, TimeStats> cachedStats = new HashMap<>();

    private StatsRepository() {
        // Mock data for demonstration
        initMockData();
    }

    public static synchronized StatsRepository getInstance() {
        if (instance == null) {
            instance = new StatsRepository();
        }
        return instance;
    }

    /**
     * Get statistics for a specific date
     */
    public TimeStats getStatsForDate(Calendar date) {
        String dateKey = formatDateKey(date);
        
        // Return cached data if available
        if (cachedStats.containsKey(dateKey)) {
            return cachedStats.get(dateKey);
        }
        
        // In a real app, you would fetch from database or API here
        
        // Return mock data for demonstration
        Calendar today = Calendar.getInstance();
        if (isSameDay(date, today)) {
            return cachedStats.get(formatDateKey(today));
        }
        
        // Return empty stats for dates without data
        return new TimeStats(date, 0, 0, 0, 0);
    }

    /**
     * Format date as a string key for the cache map
     */
    private String formatDateKey(Calendar date) {
        return String.format("%d-%d-%d", 
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH));
    }
    
    /**
     * Check if two dates represent the same day
     */
    private boolean isSameDay(Calendar date1, Calendar date2) {
        return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
               date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) &&
               date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Initialize mock data for demonstration
     */
    private void initMockData() {
        // Today's stats
        Calendar today = Calendar.getInstance();
        TimeStats todayStats = new TimeStats(
                today,
                9 * 60 * 60 * 1000 + 41 * 60 * 1000, // 9h 41m
                5 * 60 * 60 * 1000 + 32 * 60 * 1000, // 5h 32m
                3 * 60 * 60 * 1000 + 9 * 60 * 1000,  // 3h 09m
                4.5f // 4.5% increase
        );
        cachedStats.put(formatDateKey(today), todayStats);
        
        // Yesterday's stats
        Calendar yesterday = (Calendar) today.clone();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        TimeStats yesterdayStats = new TimeStats(
                yesterday,
                9 * 60 * 60 * 1000 + 15 * 60 * 1000, // 9h 15m
                5 * 60 * 60 * 1000 + 10 * 60 * 1000, // 5h 10m
                3 * 60 * 60 * 1000 + 5 * 60 * 1000,  // 3h 05m
                -2.3f // 2.3% decrease
        );
        cachedStats.put(formatDateKey(yesterday), yesterdayStats);
        
        // Last week's stats
        Calendar lastWeek = (Calendar) today.clone();
        lastWeek.add(Calendar.DAY_OF_MONTH, -7);
        TimeStats lastWeekStats = new TimeStats(
                lastWeek,
                8 * 60 * 60 * 1000 + 45 * 60 * 1000, // 8h 45m
                4 * 60 * 60 * 1000 + 30 * 60 * 1000, // 4h 30m
                3 * 60 * 60 * 1000 + 15 * 60 * 1000, // 3h 15m
                5.2f // 5.2% increase
        );
        cachedStats.put(formatDateKey(lastWeek), lastWeekStats);
    }
} 