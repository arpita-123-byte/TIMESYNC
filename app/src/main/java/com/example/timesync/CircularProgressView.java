package com.example.timesync;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircularProgressView extends View {
    private int progress = 0; // 0-100
    private int progressColor = Color.parseColor("#F44336");
    private Paint arcPaint, backgroundPaint, textPaint;
    private RectF arcRect;

    public CircularProgressView(Context context) {
        super(context);
        init();
    }
    public CircularProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(12f);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        arcPaint.setColor(progressColor);
        
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(12f);
        backgroundPaint.setColor(Color.parseColor("#EEEEEE"));
        backgroundPaint.setAlpha(80);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(progressColor);
        textPaint.setTextSize(24f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        int size = Math.min(w, h);
        if (arcRect == null) {
            float pad = 12f;
            arcRect = new RectF(pad, pad, size - pad, size - pad);
        }
        
        canvas.drawArc(arcRect, 135, 270, false, backgroundPaint);
        
        float sweep = Math.max(10f, (progress / 100f) * 270f);
        canvas.drawArc(arcRect, 135, sweep, false, arcPaint);
        canvas.drawText(progress + "%", w / 2f, h / 2f + 9f, textPaint);
    }
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
    public void setProgressColor(int color) {
        this.progressColor = color;
        arcPaint.setColor(color);
        textPaint.setColor(color);
        invalidate();
    }
} 