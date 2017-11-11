package com.technotapp.servicestation.customView;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;

public class CustomEditText extends android.support.v7.widget.AppCompatEditText {
private Context mContext;
    public CustomEditText(Context context) {
        super(context);
        mContext=context;
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        getContext().obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        String fontName = getContext().obtainStyledAttributes(attrs, R.styleable.CustomEditText).getString(R.styleable.CustomTextView_fontName);
        if (TextUtils.isEmpty(fontName))
            fontName = Constant.Fonts.IRSANS;
        Typeface type = Typeface.createFromAsset(context.getAssets(), fontName+".ttf");
        this.setTypeface(type);
    }
    public void setFontName(String fontName){
        Typeface type = Typeface.createFromAsset(mContext.getAssets(), fontName+".ttf");
        this.setTypeface(type);
    }

}
