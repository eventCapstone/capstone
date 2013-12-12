package com.angles.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * A TextView class that uses the Alpine font
 * @author Mike
 *
 */
public class AlpineTextView  extends TextView {
    public AlpineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public AlpineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public AlpineTextView(Context context) {
        super(context);
    }
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Clean_Regular.ttf"));
    }
}