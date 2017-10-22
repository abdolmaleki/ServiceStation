package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;

import in.arjsna.passcodeview.PassCodeView;

public class PinFragment extends DialogFragment implements View.OnClickListener {

    private PassCodeView mPassCodeView;
    private IPin mPinContoller;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pin, container);
        mPassCodeView = (PassCodeView) rootView.findViewById(R.id.pass_code_view);

        rootView.findViewById(R.id.fragment_pin_btn_submit).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_pin_btn_reset).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_pin_btn_abort).setOnClickListener(this);

        initView();

        return rootView;
    }

    private void initView() {
        try {

            mPassCodeView.setKeyTextColor(Color.BLACK);
            mPassCodeView.setKeyTextSize(50);
            mPassCodeView.setDigitLength(4);
            mPassCodeView.reset();

            mPassCodeView.setOnTextChangeListener(new PassCodeView.TextChangeListener() {
                @Override
                public void onTextChanged(String text) {
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(e, "PinFragment", "initView");
        }


        getDialog().setTitle("لطفا رمز اول کارت را وارد کنید");
        getDialog().getWindow().setGravity(Gravity.CENTER);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof IPin) {
            mPinContoller = (IPin) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPinContoller = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.fragment_pin_btn_submit) {
            if (isValidationPin()) {
                mPinContoller.onPinEntered(mPassCodeView.getPassCodeText());
                getDialog().cancel();
            } else {
                AppMonitor.Toast(getActivity(), getString(R.string.PinFragment_error_minpinlenght), Toast.LENGTH_SHORT);
            }

        } else if (id == R.id.fragment_pin_btn_reset) {
            mPassCodeView.reset();

        } else if (id == R.id.fragment_pin_btn_abort) {
            getDialog().cancel();
        }
    }

    private boolean isValidationPin() {
        try {
            if (mPassCodeView.getPassCodeText().length() == 4) {
                return true;
            }
        } catch (Exception e) {
            AppMonitor.reportBug(e, "PinFragment", "isValidationPin");
            return false;

        }

        return false;
    }
}
