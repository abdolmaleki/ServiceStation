package com.technotapp.servicestation.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.fragment.InputDialogFragment;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KeypadActivity extends BaseActivity implements View.OnClickListener, TextWatcher, View.OnLongClickListener {

    private TextView mTV_amount;
    private Button mBtn_keyZero;
    private Button mBtn_keyTribleZero;
    private Button mBtn_submit;

    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;

    private String mCardNumber;
    private String mAccountNumber;
    private boolean mIsActivePin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keypad);

        loadData();
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);

        txtTitle.setText("صفحه پرداخت");

        mTV_amount = findViewById(R.id.activity_keypad_tv_amount);
        mTV_amount.addTextChangedListener(this);

        mBtn_keyZero = findViewById(R.id.activity_keypad_btn_zero);
        mBtn_keyTribleZero = findViewById(R.id.activity_keypad_btn_zero_triple);
        mBtn_submit = findViewById(R.id.activity_keypad_btn_submit);

        mBtn_keyZero.setEnabled(false);
        mBtn_keyTribleZero.setEnabled(false);

        back.setOnClickListener(this);

        mBtn_submit.setOnClickListener(this);
        findViewById(R.id.activity_keypad_btn_one).setOnClickListener(this);
        findViewById(R.id.activity_keypad_btn_two).setOnClickListener(this);
        findViewById(R.id.activity_keypad_btn_three).setOnClickListener(this);
        findViewById(R.id.activity_keypad_btn_four).setOnClickListener(this);
        findViewById(R.id.activity_keypad_btn_five).setOnClickListener(this);
        findViewById(R.id.activity_keypad_btn_six).setOnClickListener(this);
        findViewById(R.id.activity_keypad_btn_seven).setOnClickListener(this);
        findViewById(R.id.activity_keypad_btn_eight).setOnClickListener(this);
        findViewById(R.id.activity_keypad_btn_nine).setOnClickListener(this);
        findViewById(R.id.activity_keypad_btn_zero_triple).setOnClickListener(this);
        findViewById(R.id.activity_keypad_btn_zero).setOnClickListener(this);
        findViewById(R.id.activity_keypad_btn_backspace).setOnClickListener(this);
        findViewById(R.id.activity_keypad_btn_backspace).setOnLongClickListener(this);

    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAccountNumber = bundle.getString(Constant.Key.ACCOUNT_NUMBER, null);
            mCardNumber = bundle.getString(Constant.Key.CARD_NUMBER, null);
            mIsActivePin = bundle.getBoolean(Constant.Key.IS_ACTIVE_PIN, false);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_keypad_btn_submit:
                if (mTV_amount.getText() == null || mTV_amount.getText().toString().equals("")) {
                    Helper.alert(this, "لطفا مبلغ را وارد کنید", Constant.AlertType.Error);
                } else {
                    insertTransaction();
                }
                break;

            case R.id.activity_keypad_btn_one:
                mTV_amount.append("1");
                break;
            case R.id.activity_keypad_btn_two:
                mTV_amount.append("2");
                break;
            case R.id.activity_keypad_btn_three:
                mTV_amount.append("3");
                break;

            case R.id.activity_keypad_btn_four:
                mTV_amount.append("4");
                break;
            case R.id.activity_keypad_btn_five:
                mTV_amount.append("5");
                break;
            case R.id.activity_keypad_btn_six:
                mTV_amount.append("6");
                break;

            case R.id.activity_keypad_btn_seven:
                mTV_amount.append("7");
                break;
            case R.id.activity_keypad_btn_eight:
                mTV_amount.append("8");
                break;
            case R.id.activity_keypad_btn_nine:
                mTV_amount.append("9");
                break;

            case R.id.activity_keypad_btn_zero_triple:
                mTV_amount.append("000");
                break;
            case R.id.activity_keypad_btn_zero:
                mTV_amount.append("0");
                break;
            case R.id.activity_keypad_btn_backspace:
                if (mTV_amount.getText().toString().length() <= 1) {
                    mBtn_keyZero.setEnabled(false);
                    mBtn_keyTribleZero.setEnabled(false);
                }
                if (mTV_amount.getText().toString().length() > 0) {
                    mTV_amount.setText(mTV_amount.getText().toString().substring(0, mTV_amount.getText().toString().length() - 1));
                }
                break;
        }

        if (!mTV_amount.getText().toString().isEmpty()) {
            mBtn_keyZero.setEnabled(true);
            mBtn_keyTribleZero.setEnabled(true);
        }

        if (view.getId() == back.getId()) {
            finish();
        }
    }

    private void insertTransaction() {
        if (mIsActivePin) {
            InputDialogFragment inputDialogFragment = InputDialogFragment.newInstance("رمز عبور را وارد کنید", Color.BLUE);
            inputDialogFragment.show(this.getFragmentManager(), "input");
            inputDialogFragment.setOnInputDialogClickListener(new InputDialogFragment.OnInputDialogClick() {
                @Override
                public void onAccept(String password) {
                    inputDialogFragment.dismiss();
                    //callTransaction();
                }
            });
        } else {

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    private String current = "";

    @Override
    public void afterTextChanged(Editable editable) {
        try {
            mTV_amount.removeTextChangedListener(this);
            if (!mTV_amount.getText().toString().trim().equals("")) {
                if (!editable.toString().equals(current)) {

                    String cleanString = editable.toString().replaceAll("[,]", "");
                    String formatted = String.format(Locale.CANADA, "%,d", Long.parseLong(cleanString));
                    current = formatted;
                    mTV_amount.setText(formatted);
                }
            }
            mTV_amount.addTextChangedListener(this);
        } catch (Exception e) {
            AppMonitor.reportBug(this, e, "KeypadActivity", "afterTextChanged");
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.activity_keypad_btn_backspace) {
            mBtn_keyZero.setEnabled(false);
            mBtn_keyTribleZero.setEnabled(false);
            mTV_amount.setText("");
        }
        return false;
    }
}
