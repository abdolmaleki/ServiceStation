package com.technotapp.servicestation.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technotapp.servicestation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentGatewayActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gatway);

        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        txtTitle.setText("درگاه پرداخت");
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == back.getId()) {
            finish();
        }
    }
}
