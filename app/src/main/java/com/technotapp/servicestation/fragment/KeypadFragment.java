package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.setting.Session;

import java.util.Locale;

public class KeypadFragment extends DialogFragment implements View.OnClickListener, View.OnLongClickListener {
    private TransactionDataModel transactionDataModel;
    private TextView tvAmount;
    private Button btnZero;
    private Button btnTripleZero;
    private Session mSession;
    private KeypadListener mKeypadListener;
    private TextView mTV_amount;
    private boolean mIsActivePin;

    public static KeypadFragment newInstance(boolean isActivePin) {
        KeypadFragment fragment = new KeypadFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constant.Key.IS_ACTIVE_PIN, isActivePin);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            View rootView = inflater.inflate(R.layout.fragment_card_service_deposit_and_buy, container, false);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);
            Window window = getDialog().getWindow();
            window.setLayout(LinearLayout.
                    LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            loadData();
            initView(rootView);

            return rootView;
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "KeypadFragment", "onCreateView");
            return null;
        }
    }

    private void loadData() {
        try {
            Bundle bundle = getArguments();
            mIsActivePin = bundle.getBoolean(Constant.Key.IS_ACTIVE_PIN, false);

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "KeypadFragment", "loadData");
        }
    }

    private void initView(View v) {
        try {
            tvAmount = (TextView) v.findViewById(R.id.fragment_card_service_buy_txtAmount);

            btnZero = v.findViewById(R.id.fragment_keypad_triple_zero);
            btnTripleZero = v.findViewById(R.id.fragment_keypad_zero);
            btnZero.setEnabled(false);
            btnTripleZero.setEnabled(false);
            (v.findViewById(R.id.fragment_card_service_buy_btn)).setOnClickListener(this);

            (v.findViewById(R.id.fragment_keypad_one)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_two)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_three)).setOnClickListener(this);

            (v.findViewById(R.id.fragment_keypad_four)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_five)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_six)).setOnClickListener(this);

            (v.findViewById(R.id.fragment_keypad_seven)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_eight)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_nine)).setOnClickListener(this);

            (v.findViewById(R.id.fragment_keypad_triple_zero)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_zero)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_backspace)).setOnClickListener(this);

            (v.findViewById(R.id.fragment_keypad_backspace)).setOnLongClickListener(this);

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "KeypadFragment", "initView");
        }


        tvAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            private String current = "";

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    tvAmount.removeTextChangedListener(this);
                    if (!tvAmount.getText().toString().trim().equals("")) {
                        if (!s.toString().equals(current)) {

                            String cleanString = s.toString().replaceAll("[,]", "");
                            String formatted = String.format(Locale.CANADA, "%,d", Long.parseLong(cleanString));
                            current = formatted;
                            tvAmount.setText(formatted);
                        }
                    }
                    tvAmount.addTextChangedListener(this);
                } catch (Exception e) {
                    AppMonitor.reportBug(getActivity(), e, "KeypadFragment", "afterTextChanged");
                }
            }
        });

        mSession = Session.getInstance(getActivity());


    }

    private void initTransactionModel() {
        try {

            transactionDataModel = new TransactionDataModel();
            //TODO setTerminalID
            transactionDataModel.setTerminalID(mSession.getTerminalId());
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "KeypadFragment", "initTransactionModel");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_card_service_buy_btn:
                returnResult();
                break;

            case R.id.fragment_keypad_one:
                tvAmount.append("1");
                break;
            case R.id.fragment_keypad_two:
                tvAmount.append("2");
                break;
            case R.id.fragment_keypad_three:
                tvAmount.append("3");
                break;

            case R.id.fragment_keypad_four:
                tvAmount.append("4");
                break;
            case R.id.fragment_keypad_five:
                tvAmount.append("5");
                break;
            case R.id.fragment_keypad_six:
                tvAmount.append("6");
                break;

            case R.id.fragment_keypad_seven:
                tvAmount.append("7");
                break;
            case R.id.fragment_keypad_eight:
                tvAmount.append("8");
                break;
            case R.id.fragment_keypad_nine:
                tvAmount.append("9");
                break;

            case R.id.fragment_keypad_triple_zero:
                tvAmount.append("000");
                break;
            case R.id.fragment_keypad_zero:
                tvAmount.append("0");
                break;
            case R.id.fragment_keypad_backspace:
                if (tvAmount.getText().toString().length() <= 1) {
                    btnZero.setEnabled(false);
                    btnTripleZero.setEnabled(false);
                }
                if (tvAmount.getText().toString().length() > 0) {
                    tvAmount.setText(tvAmount.getText().toString().substring(0, tvAmount.getText().toString().length() - 1));
                }
                break;
        }

        if (!tvAmount.getText().toString().isEmpty()) {
            btnZero.setEnabled(true);
            btnTripleZero.setEnabled(true);
        }


    }

    private void returnResult() {
        if (tvAmount.getText() == null || tvAmount.getText().toString().equals("")) {
            Helper.alert(getActivity(), "لطفا مبلغ را وارد کنید", Constant.AlertType.Error);
        } else {
            if (mIsActivePin) {
                InputDialogFragment inputDialogFragment = InputDialogFragment.newInstance("رمز حساب را وارد کنید", Color.BLUE, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                inputDialogFragment.show(this.getFragmentManager(), "input");
                inputDialogFragment.setOnInputDialogClickListener(new InputDialogFragment.OnInputDialogClick() {
                    @Override
                    public void onAccept(String password) {
                        mKeypadListener.onAmountEntered(tvAmount.getText().toString().replaceAll("[,]", ""), password);
                        inputDialogFragment.dismiss();
                    }
                });
            } else {
                mKeypadListener.onAmountEntered(tvAmount.getText().toString().replaceAll("[,]", ""), null);
            }
            dismiss();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.fragment_keypad_backspace) {
            btnZero.setEnabled(false);
            btnTripleZero.setEnabled(false);
            tvAmount.setText("");
        }
        return false;
    }

    public void show(Activity activity, KeypadListener keypadListener) {
        mKeypadListener = keypadListener;
        this.show(activity.getFragmentManager(), this.getClass().getName());
    }

    public interface KeypadListener {
        void onAmountEntered(String amount, String password);
    }
}
