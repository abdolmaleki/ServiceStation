package com.technotapp.servicestation.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.technotapp.servicestation.R;


public class CustomButton extends android.support.v7.widget.AppCompatButton {

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomButton);
        String fontName = a.getString(R.styleable.CustomButton_fontName);
        if (TextUtils.isEmpty(fontName))
            fontName = "irsans";
        Typeface type = Typeface.createFromAsset(context.getAssets(), fontName+".ttf");
        this.setTypeface(type);
    }

}
