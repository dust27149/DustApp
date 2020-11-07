package com.dust.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class LoadingView extends View {

    private int width;
    private int height;
    private int rectWidth;
    private int rectHeight;
    private Paint rectPaint;
    private int pos = 0;
    private RectF rectF;
    private final String[] color = {"#bbbbbb", "#aaaaaa", "#999999", "#888888", "#777777", "#666666",};

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            width = 200;
        } else {
            width = height = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        }
        rectWidth = width / 12;
        rectHeight = 4 * rectWidth;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rectF == null) {
            rectF = new RectF((width - rectWidth) / 2.0f, 0, (width + rectWidth) / 2.0f, rectHeight);
        }
        for (int i = 0; i < 12; i++) {
            if (i - pos >= 5) {
                rectPaint.setColor(Color.parseColor(color[5]));
            } else if (i - pos >= 0 && i - pos < 5) {
                rectPaint.setColor(Color.parseColor(color[i - pos]));
            } else if (i - pos >= -7 && i - pos < 0) {
                rectPaint.setColor(Color.parseColor(color[5]));
            } else if (i - pos >= -11 && i - pos < -7) {
                rectPaint.setColor(Color.parseColor(color[12 + i - pos]));
            }
            canvas.drawRoundRect(rectF, rectHeight / 2.0f, rectHeight / 2.0f, rectPaint);
            canvas.rotate(30, (int) (width / 2.0), (int) (width / 2.0));
        }
        pos++;
        if (pos > 11) {
            pos = 0;
        }
        postInvalidateDelayed(100);
    }

}