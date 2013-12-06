package com.angles.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class AlpineButton  extends Button {
    public AlpineButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public AlpineButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public AlpineButton(Context context) {
        super(context);
    }
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Clean_Regular.ttf"));
    }
}