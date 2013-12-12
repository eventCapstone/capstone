package com.angles.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * A CheckBox class that uses the Alpine font
 * @author Mike
 *
 */
public class AlpineCheckBox  extends CheckBox {
    public AlpineCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public AlpineCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public AlpineCheckBox(Context context) {
        super(context);
    }
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Clean_Regular.ttf"));
    }
}