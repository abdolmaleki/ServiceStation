package com.technotapp.servicestation.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.customView.TextViewEx;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        iniView();
    }

    private void iniView() {
        ButterKnife.bind(this);
        TextViewEx mTV_description = findViewById(R.id.activity_about_tv_description);
        mTV_description.setText("شرکت تکنوتَپ یکی از شرکت های دانش بنیان می باشد که در سال 1395 تاسیس شد. عمده فعالیت های این شرکت نرم افزاری بر مبنای نرم افزارهای حوزه پرداخت الکترونیکی و هوشمندسازی خودرو می باشد. البته شرکت تکنوتَپ دستاوردهای خوبی در زمینه اختراعات و تولیدات سخت افزاری داشته است. مدیرعامل این شرکت آقای محمودی می باشد.", true);
        txtTitle.setText("درباره نرم افزار");
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == back.getId()) {
            finish();
        }
    }
}
