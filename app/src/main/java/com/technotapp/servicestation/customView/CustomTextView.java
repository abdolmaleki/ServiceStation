package com.technotapp.servicestation.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
       super(context, attrs);
        getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String fontName = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView).getString(R.styleable.CustomTextView_fontName);
        if (TextUtils.isEmpty(fontName))
            fontName = Constant.Fonts.IRSANS;
        Typeface type = Typeface.createFromAsset(context.getAssets(), fontName+".ttf");
        this.setTypeface(type);
    }

}
