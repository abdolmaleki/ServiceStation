package com.technotapp.servicestation.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.technotapp.servicestation.R;

public class ExitConfirmDialog extends DialogFragment implements View.OnClickListener {

    private OnExitListener mOnExitListener;

    public static ExitConfirmDialog newInstance() {
        ExitConfirmDialog fragment = new ExitConfirmDialog();
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
        View rootView = inflater.inflate(R.layout.fragment_dialog_exit_confrim, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {
        rootView.findViewById(R.id.fragment_dialog_exit_confirm_btn_accept).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_dialog_exit_confirm_btn_cancel).setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_dialog_exit_confirm_btn_cancel) {
            dismiss();
        } else if (v.getId() == R.id.fragment_dialog_exit_confirm_btn_accept) {
            mOnExitListener.onAccept();
        }
    }

    public void setOnDialogClick(OnExitListener onInputDialogClick) {
        mOnExitListener = onInputDialogClick;
    }

    public interface OnExitListener {
        void onAccept();
    }
}
