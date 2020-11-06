package com.dust.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

import com.dust.app.R;

import static android.view.animation.Animation.REVERSE;

public class CodeEditText extends AppCompatEditText {

    public interface OnTextChangeListener {

        void onTextChange(CharSequence text, int length);

        void onTextFinish(CharSequence text, int length);

    }

    private int textColor;
    private int maxLength = 4;
    private int strokeWidth;
    private int strokeHeight;
    private int strokePadding = 20;
    private final Rect rect = new Rect();
    private OnTextChangeListener onTextChangeListener;
    private Drawable stokeError;
    private Drawable strokeActive;
    private Drawable strokeBackground;
    private Drawable strokeForeground;

    public CodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CodeEditText);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            if (index == R.styleable.CodeEditText_strokeHeight) {
                strokeHeight = (int) typedArray.getDimension(index, 60);
            } else if (index == R.styleable.CodeEditText_strokeWidth) {
                strokeWidth = (int) typedArray.getDimension(index, 60);
            } else if (index == R.styleable.CodeEditText_strokePadding) {
                strokePadding = (int) typedArray.getDimension(index, 20);
            } else if (index == R.styleable.CodeEditText_strokeBackground) {
                strokeBackground = typedArray.getDrawable(index);
            } else if (index == R.styleable.CodeEditText_strokeLength) {
                maxLength = typedArray.getInteger(index, 4);
            } else if (index == R.styleable.CodeEditText_strokeForeground) {
                strokeForeground = strokeActive = typedArray.getDrawable(index);
            } else if (index == R.styleable.CodeEditText_strokeError) {
                stokeError = typedArray.getDrawable(index);
            }
        }
        typedArray.recycle();
        if (strokeBackground == null) {
            throw new NullPointerException("stroke drawable background not allowed to be null!");
        }
        if (strokeForeground == null) {
            throw new NullPointerException("stroke drawable foreground not allowed to be null!");
        }
        if (stokeError == null) {
            throw new NullPointerException("stroke drawable error not allowed to be null!");
        }
        setMaxLength(maxLength);
        setLongClickable(false);
        setBackgroundColor(Color.TRANSPARENT);
        setCursorVisible(false);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        return false;
    }

//    private int px(int size) {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
//    }

    private void setMaxLength(int maxLength) {
        if (maxLength >= 0) {
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        } else {
            setFilters(new InputFilter[0]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (height < strokeHeight) {
            height = strokeHeight;
        }
        int recommendWidth = strokeWidth * maxLength + strokePadding * (maxLength - 1);
        if (width < recommendWidth) {
            width = recommendWidth;
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        textColor = getCurrentTextColor();
        setTextColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        setTextColor(textColor);
        drawStrokeBackground(canvas);
        drawText(canvas);
    }

    private void drawStrokeBackground(Canvas canvas) {
        rect.left = 0;
        rect.top = canvas.getHeight() - strokeHeight;
        rect.right = strokeWidth;
        rect.bottom = canvas.getHeight();
        int count = canvas.getSaveCount();
        canvas.save();
        for (int i = 0; i < maxLength; i++) {
            strokeBackground.setBounds(rect);
            strokeBackground.setState(new int[]{android.R.attr.state_enabled});
            strokeBackground.draw(canvas);
            float dx = rect.right + strokePadding;
            canvas.translate(dx, 0);
        }
        canvas.restoreToCount(count);
        int activatedIndex = Math.max(0, getEditableText().length());
        canvas.save();
        for (int i = 0; i < activatedIndex; i++) {
            strokeForeground.setBounds(rect);
            strokeForeground.setState(new int[]{android.R.attr.state_enabled});
            strokeForeground.draw(canvas);
            float dx = rect.right + strokePadding;
            canvas.translate(dx, 0);
        }
        canvas.restoreToCount(count);
    }

    private void drawText(Canvas canvas) {
        int count = canvas.getSaveCount();
        int length = getEditableText().length();
        for (int i = 0; i < length; i++) {
            String text = String.valueOf(getEditableText().charAt(i));
            TextPaint textPaint = getPaint();
            textPaint.setColor(textColor);
            textPaint.getTextBounds(text, 0, 1, rect);
            int x = strokeWidth / 2 + (strokeWidth + strokePadding) * i - (rect.centerX());
            int y = canvas.getHeight() / 2 + rect.height() / 2;
            canvas.drawText(text, x, y, textPaint);
        }
        canvas.restoreToCount(count);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        int textLength = getEditableText().length();
        if (textLength == maxLength) {
            hideSoftInput();
        }
        if (onTextChangeListener != null) {
            if (!(lengthAfter == 0 && lengthBefore == maxLength)) {
                onTextChangeListener.onTextChange(getEditableText().toString(), textLength);
            }
            if (textLength == maxLength) {
                onTextChangeListener.onTextFinish(getEditableText().toString(), maxLength);
            }
        }
    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener;
    }

    public void setStokeError() {
        strokeForeground = stokeError;
        TranslateAnimation animation = new TranslateAnimation(-strokePadding, strokePadding, 0, 0);
        animation.setDuration(100);
        animation.setRepeatCount(2);
        animation.setRepeatMode(REVERSE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setText(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animation);
    }

    public void resetStokeError() {
        strokeForeground = strokeActive;
    }
}
