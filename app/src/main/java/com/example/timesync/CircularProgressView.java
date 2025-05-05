package com.example.timesync;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircularProgressView extends View {
    private float progress = 56.9f; // Default progress value
    private int startColor = Color.parseColor("#2196F3"); // Blue
    private int endColor = Color.parseColor("#FF4B55"); // Red
    private final Paint circlePaint = new Paint();
    private final Paint progressPaint = new Paint();
    private final RectF circleRect = new RectF();

    public CircularProgressView(Context context) {
        super(context);
        init();
    }
    public CircularProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        // Initialize paints
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.parseColor("#1F2937"));
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(25f);
        
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(25f);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Update the circle bounds when size changes
        int padding = (int) progressPaint.getStrokeWidth() / 2 + 10;
        circleRect.set(padding, padding, w - padding, h - padding);
        
        // Create gradient for progress arc
        SweepGradient gradient = new SweepGradient(
                circleRect.centerX(),
                circleRect.centerY(),
                new int[]{startColor, endColor},
                null);
        progressPaint.setShader(gradient);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        // Draw background circle
        canvas.drawArc(circleRect, 0, 360, false, circlePaint);
        
        // Draw progress arc (0-100%)
        float sweepAngle = (progress / 100) * 300; // 300 degrees arc (not full circle)
        canvas.drawArc(circleRect, 120, sweepAngle, false, progressPaint);
    }
    /**
     * Set the progress value (0-100)
     */
    public void setProgress(float progress) {
        this.progress = Math.max(0, Math.min(100, progress));
        invalidate();
    }
    /**
     * Get the current progress value
     */
    public float getProgress() {
        return progress;
    }
    /**
     * Set the progress colors
     */
    public void setProgressColors(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        
        // Update gradient
        SweepGradient gradient = new SweepGradient(
                circleRect.centerX(),
                circleRect.centerY(),
                new int[]{startColor, endColor},
                null);
        progressPaint.setShader(gradient);
        invalidate();
    }
} 