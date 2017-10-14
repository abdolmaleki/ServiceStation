package com.technotapp.servicestation.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.technotapp.servicestation.R;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String fontName = a.getString(R.styleable.CustomTextView_fontName);
        if (TextUtils.isEmpty(fontName))
            fontName = "irsans";
        Typeface type = Typeface.createFromAsset(context.getAssets(), fontName+".ttf");
        this.setTypeface(type);
    }

}
