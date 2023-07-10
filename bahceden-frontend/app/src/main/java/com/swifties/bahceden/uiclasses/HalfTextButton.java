package com.swifties.bahceden.uiclasses;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.appcompat.widget.AppCompatButton;

public class HalfTextButton extends AppCompatButton {

    public HalfTextButton(Context context) {
        super(context);
    }

    public HalfTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HalfTextButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        setTextSize(TypedValue.COMPLEX_UNIT_PX, Math.min(width, height) / 3f);
    }
}