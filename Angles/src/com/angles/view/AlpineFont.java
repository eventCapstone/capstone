package com.angles.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class AlpineFont  extends TextView {
    public AlpineFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public AlpineFont(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public AlpineFont(Context context) {
        super(context);
    }
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/A1_Regular.ttf"));
    }
}