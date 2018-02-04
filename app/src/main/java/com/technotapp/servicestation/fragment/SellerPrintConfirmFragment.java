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

public class SellerPrintConfirmFragment extends DialogFragment implements View.OnClickListener {

    private OnSellerPrintDialogClick mOnDialogClick;

    public static SellerPrintConfirmFragment newInstance() {
        SellerPrintConfirmFragment fragment = new SellerPrintConfirmFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_seller_print_confrim, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);
        rootView.findViewById(R.id.fragment_dialog_seller_print_confirm_btn_accept).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_dialog_seller_print_confirm_btn_cancel).setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_dialog_seller_print_confirm_btn_cancel) {
            dismiss();
        } else if (v.getId() == R.id.fragment_dialog_seller_print_confirm_btn_accept) {
            mOnDialogClick.onAccept();
        }
    }

    public void setOnDialogButtonClick(OnSellerPrintDialogClick onInputDialogClick) {
        mOnDialogClick = onInputDialogClick;
    }

    public interface OnSellerPrintDialogClick {
        void onAccept();
    }
}
