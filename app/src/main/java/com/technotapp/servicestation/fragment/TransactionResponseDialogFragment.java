package com.technotapp.servicestation.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;

public class TransactionResponseDialogFragment extends DialogFragment {
    View background;
    ImageView img;
    TextView tvTransactionResponse;
    TextView tvTransactionExtraMessage;
    Button btnPositive;
    Button btnNegative;

    Boolean hasSellerReceipt;
    Boolean isSuccess;
    String extraMessage;
    MyOnClickListener mMyOnClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {

            View rootView = inflater.inflate(R.layout.fragment_dialog_transaction_response, container, false);
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            }
            initView(rootView);
            initVariable();
            initLayout();
            btnNegative.setOnClickListener(mMyOnClickListener);
            btnPositive.setOnClickListener(mMyOnClickListener);

            return rootView;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "TransactionResponseDialogFragment", "onCreateView");
            return null;
        }


    }

    private void initLayout() {
        try {
            if (isSuccess) {
                background.setBackgroundColor(getResources().getColor(R.color.fragment_transaction_response_dialog_successBackgroungColor));
                img.setImageResource(R.drawable.success);
                tvTransactionResponse.setText("تراکنش موفق");
                if (hasSellerReceipt) {
                    btnNegative.setVisibility(View.VISIBLE);
                    tvTransactionExtraMessage.setVisibility(View.VISIBLE);

                    tvTransactionExtraMessage.setText(extraMessage);
                    tvTransactionResponse.setTextColor(getResources().getColor(R.color.fragment_transaction_response_dialog_success_textColor));

                    btnPositive.setText("بله");
                } else {
                    btnNegative.setVisibility(View.GONE);
                    tvTransactionExtraMessage.setVisibility(View.GONE);
                    btnPositive.setText("تایید");


                }
            } else {
                btnNegative.setVisibility(View.GONE);
                tvTransactionExtraMessage.setVisibility(View.GONE);
                background.setBackgroundColor(getResources().getColor(R.color.fragment_transaction_response_dialog_failBackgroungColor));
                img.setImageResource(R.drawable.fail);
                tvTransactionResponse.setText("تراکنش ناموفق");
                if (!extraMessage.isEmpty()) {
                    tvTransactionExtraMessage.setVisibility(View.VISIBLE);
                    tvTransactionResponse.setTextColor(getResources().getColor(R.color.fragment_transaction_response_dialog_fail_textColor));
                    tvTransactionExtraMessage.setText(extraMessage);
                }
                btnPositive.setText("تایید");
            }
        } catch (Exception e) {
            AppMonitor.reportBug(e, "TransactionResponseDialogFragment", "initLayout");
        }
    }

    private void initVariable() {
        try {
            Bundle bundle = getArguments();
            isSuccess = bundle.getBoolean("isSuccess", false);
            hasSellerReceipt = bundle.getBoolean("hasSellerReceipt", false);
            extraMessage = bundle.getString("extraMessage", null);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "TransactionResponseDialogFragment", "initVariable");
        }

    }

    private void initView(View v) {
        try {
            background = v.findViewById(R.id.fragment_dialog_transaction_response_background);
            img = (ImageView) v.findViewById(R.id.fragment_dialog_transaction_response_imgIcon);
            tvTransactionResponse = (TextView) v.findViewById(R.id.fragment_dialog_transaction_response_txtTransactionResponse);
            tvTransactionExtraMessage = (TextView) v.findViewById(R.id.fragment_dialog_transaction_response_txtExtraMessage);
            btnPositive = (Button) v.findViewById(R.id.fragment_dialog_transaction_response_btnPositive);
            btnNegative = (Button) v.findViewById(R.id.fragment_dialog_transaction_response_btnNegative);

        } catch (Exception e) {
            AppMonitor.reportBug(e, "TransactionResponseDialogFragment", "initView");
        }

    }

    public void onClickListener(MyOnClickListener listener) {
        mMyOnClickListener = listener;
    }


    public interface MyOnClickListener extends View.OnClickListener {

    }
}
