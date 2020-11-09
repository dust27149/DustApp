package com.dust.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatButton;

import com.dust.app.R;

public class LoadingButton extends AppCompatButton {

    private int width;
    private int height;
    private int dotSize;
    private int dotPadding;
    private String text;
    private boolean loading;
    private Paint paint;
    private int pos = 0;
    private final String[] color = {"#00000000", "#00000000", "#00000000", "#CCFFFFFF", "#88FFFFFF", "#44FFFFFF"};

    public LoadingButton(Context context) {
        this(context, null);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            if (index == R.styleable.LoadingButton_dotSize) {
                dotSize = (int) typedArray.getDimension(index, 22);
            } else if (index == R.styleable.LoadingButton_dotPadding) {
                dotPadding = (int) typedArray.getDimension(index, 44);
            } else if (index == R.styleable.LoadingButton_loading) {
                loading = typedArray.getBoolean(index, false);
            }
        }
        setGravity(Gravity.CENTER);
        typedArray.recycle();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        text = getText().toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = getWidth();
        height = getHeight();
        int recommendWidth = 4 * dotPadding + 6 * dotSize;
        int recommendHeight = 4 * dotSize;
        if (width < recommendWidth) {
            width = recommendWidth;
        }
        if (height < recommendHeight) {
            height = recommendHeight;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDots(canvas);
    }

    private void drawDots(Canvas canvas) {
        if (!loading) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            if (i + pos < 6) {
                paint.setColor(Color.parseColor(color[i + pos]));
            } else {
                paint.setColor(Color.parseColor(color[i + pos - 6]));
            }
            canvas.drawCircle(width / 2.0f - dotPadding * (i - 1), height / 2.0f, dotSize, paint);
        }
        pos++;
        if (pos > 5) {
            pos = 0;
        }
        postInvalidateDelayed(200);
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        if (loading) {
            setText(null);
        } else {
            setText(text);
        }
    }

}