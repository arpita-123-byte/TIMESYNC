package com.example.timesync;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircularScoreView extends View {
    private float score = 0f; // 0-100
    private int productiveColor = Color.parseColor("#2196F3");
    private int unproductiveColor = Color.parseColor("#F44336");
    private Paint arcPaint, textPaint, labelPaint;
    private RectF arcRect;

    public CircularScoreView(Context context) {
        super(context);
        init();
    }
    public CircularScoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(18f);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(64f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setColor(Color.LTGRAY);
        labelPaint.setTextSize(32f);
        labelPaint.setTextAlign(Paint.Align.CENTER);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        int size = Math.min(w, h);
        if (arcRect == null) {
            float pad = 32f;
            arcRect = new RectF(pad, pad, size - pad, size - pad);
        }
        float productiveSweep = (score / 100f) * 270f;
        float unproductiveSweep = 270f - productiveSweep;
        // Draw productive arc
        arcPaint.setColor(productiveColor);
        canvas.drawArc(arcRect, 135, productiveSweep, false, arcPaint);
        // Draw unproductive arc
        arcPaint.setColor(unproductiveColor);
        canvas.drawArc(arcRect, 135 + productiveSweep, unproductiveSweep, false, arcPaint);
        // Draw score text
        canvas.drawText(String.format("%.1f", score), w / 2f, h / 2f + 24f, textPaint);
        // Draw label
        canvas.drawText("Total Score", w / 2f, h / 2f - 32f, labelPaint);
        canvas.drawText("Productive", w / 2f, h / 2f + 64f, labelPaint);
    }
    public void setScore(float score) {
        this.score = score;
        invalidate();
    }
    public void setProductiveColor(int color) {
        this.productiveColor = color;
        invalidate();
    }
    public void setUnproductiveColor(int color) {
        this.unproductiveColor = color;
        invalidate();
    }
} 