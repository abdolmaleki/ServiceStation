package com.technotapp.servicestation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;


public class KeypadFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    private static TextView tv;
    private boolean isFirst = true;

    private Button btnZero;
    private Button btnTripleZero;

    public static KeypadFragment newInstance(TextView textView) {
        KeypadFragment fragment = new KeypadFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        tv = textView;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rooView = inflater.inflate(R.layout.fragment_keypad, container, false);
        initView(rooView);
        return rooView;
    }

    private void initView(View v) {


        btnZero = (Button) v.findViewById(R.id.fragment_keypad_triple_zero);
        btnTripleZero = (Button) v.findViewById(R.id.fragment_keypad_zero);
        btnZero.setEnabled(false);
        btnTripleZero.setEnabled(false);
        tv.addTextChangedListener(new TextWatcher() {
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
                    tv.removeTextChangedListener(this);
                    if (!tv.getText().toString().trim().equals("")) {
                        if (!s.toString().equals(current)) {

                            String cleanString = s.toString().replaceAll("[,]", "");
                            String formatted = String.format("%,d", Long.parseLong(cleanString));

                            // formatted = getString(R.string.fragment_payment_hint_amount);

                            current = formatted;
                            tv.setText(formatted);
                        }
                    }
                    tv.addTextChangedListener(this);
                } catch (Exception ex) {

                }
            }
        });
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


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_keypad_one:
                tv.append("1");
                break;
            case R.id.fragment_keypad_two:
                tv.append("2");
                break;
            case R.id.fragment_keypad_three:
                tv.append("3");
                break;

            case R.id.fragment_keypad_four:
                tv.append("4");
                break;
            case R.id.fragment_keypad_five:
                tv.append("5");
                break;
            case R.id.fragment_keypad_six:
                tv.append("6");
                break;

            case R.id.fragment_keypad_seven:
                tv.append("7");
                break;
            case R.id.fragment_keypad_eight:
                tv.append("8");
                break;
            case R.id.fragment_keypad_nine:
                tv.append("9");
                break;

            case R.id.fragment_keypad_triple_zero:
                tv.append("000");
                break;
            case R.id.fragment_keypad_zero:
                tv.append("0");
                break;
            case R.id.fragment_keypad_backspace:
                if (tv.getText().toString().length() <= 1) {
                    btnZero.setEnabled(false);
                    btnTripleZero.setEnabled(false);
                }
                if (tv.getText().toString().length() > 0) {
                    tv.setText(tv.getText().toString().substring(0, tv.getText().toString().length() - 1));
                }
                break;
        }

        if (!tv.getText().toString().isEmpty()) {
            btnZero.setEnabled(true);
            btnTripleZero.setEnabled(true);
        }


    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.fragment_keypad_backspace) {
            btnZero.setEnabled(false);
            btnTripleZero.setEnabled(false);
            tv.setText("");
        }
        return false;
    }
}
