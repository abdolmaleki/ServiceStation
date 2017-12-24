package com.technotapp.servicestation.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Converters;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;

public class CashPaymentConfirmDialog extends DialogFragment implements View.OnClickListener {

    private OnCashPaymentDialogClick mOnCashPaymentDialogClick;
    private long mFactorTotalPrice;

    public static CashPaymentConfirmDialog newInstance(long factorTotalPrice) {
        CashPaymentConfirmDialog fragment = new CashPaymentConfirmDialog();
        Bundle args = new Bundle();
        args.putLong(Constant.Key.FACTOR_TOTAL_PRICE, factorTotalPrice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDate();
    }

    private void loadDate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mFactorTotalPrice = bundle.getLong(Constant.Key.FACTOR_TOTAL_PRICE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_cashpayment_confrim, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);
        rootView.findViewById(R.id.fragment_dialog_cashpayment_confirm_btn_accept).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_dialog_cashpayment_confirm_btn_cancel).setOnClickListener(this);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {
        try {
            ((TextView) rootView.findViewById(R.id.fragment_dialog_cashpayment_confirm_tv_message)).setText("آیا پرداخت مبلغ " + Converters.convertEnDigitToPersian(String.valueOf(mFactorTotalPrice)) + " ریال به صورت نقدی را تایید می کنید؟");

        } catch (Exception e) {
            AppMonitor.reportBug(e, "CashPaymentConfirmDialog", "initView");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_dialog_cashpayment_confirm_btn_cancel) {
            dismiss();
        } else if (v.getId() == R.id.fragment_dialog_cashpayment_confirm_btn_accept) {
            mOnCashPaymentDialogClick.onAccept();
        }
    }

    public void setmOnCashPaymentDialogClick(OnCashPaymentDialogClick onInputDialogClick) {
        mOnCashPaymentDialogClick = onInputDialogClick;
    }

    public interface OnCashPaymentDialogClick {
        void onAccept();
    }
}
