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

import com.technotapp.servicestation.R;

public class TransactionResponseDialogFragment extends DialogFragment {
    View background;
    ImageView img;
    TextView tvTransactionResponse;
    TextView tvTransactionExtraMessage;
    Button btnPosetive;
    Button btnNegative;

    Boolean hasRecipt;
    String extraMessage;
    MyOnClickListener mMyOnClickListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_response_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        initView(rootView);
        initVariable();
        initLayout();
        btnNegative.setOnClickListener(mMyOnClickListener);
        btnPosetive.setOnClickListener(mMyOnClickListener);

        return rootView;


    }

    private void initLayout() {
        if (hasRecipt) {
            btnNegative.setVisibility(View.VISIBLE);
            btnPosetive.setText("بله");
            tvTransactionExtraMessage.setVisibility(View.VISIBLE);
            tvTransactionExtraMessage.setText(extraMessage);
        } else {

            btnNegative.setVisibility(View.GONE);
            tvTransactionExtraMessage.setVisibility(View.GONE);
            btnPosetive.setText("تایید");

        }
    }

    private void initVariable() {
        Bundle bundle = getArguments();
        hasRecipt = bundle.getBoolean("hasRecipt");
        extraMessage = bundle.getString("extraMessage");

    }

    private void initView(View v) {
        background = (View) v.findViewById(R.id.fragment_transaction_response_dialog_background);
        img = (ImageView) v.findViewById(R.id.fragment_transaction_response_dialog_imgIcon);
        tvTransactionResponse = (TextView) v.findViewById(R.id.fragment_transaction_response_dialog_txtTransactionResponse);
        tvTransactionExtraMessage = (TextView) v.findViewById(R.id.fragment_transaction_response_dialog_txtExtraMessage);
        btnPosetive = (Button) v.findViewById(R.id.fragment_transaction_response_dialog_btnPositive);
        btnNegative = (Button) v.findViewById(R.id.fragment_transaction_response_dialog_btnNegative);

    }

    public void onClickListener(MyOnClickListener listener) {
        mMyOnClickListener = listener;
    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.fragment_transaction_response_dialog_btnPositive:
//
//                break;
//            case R.id.fragment_transaction_response_dialog_btnNegative:
//                dismiss();
//                break;
//
//        }
//
//    }

    public interface MyOnClickListener extends View.OnClickListener {

    }
}
