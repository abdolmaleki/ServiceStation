package com.technotapp.servicestation.customView;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;


public class CustomCheckbox extends android.support.v7.widget.AppCompatCheckBox {

    public CustomCheckbox(Context context) {
        super(context);
    }

    public CustomCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        String fontName = getContext().obtainStyledAttributes(attrs, R.styleable.CustomButton).getString(R.styleable.CustomButton_fontName);
        if (TextUtils.isEmpty(fontName))
            fontName = Constant.Fonts.IRSANS;
        Typeface type = Typeface.createFromAsset(context.getAssets(), fontName+".ttf");
        this.setTypeface(type);
    }
}
