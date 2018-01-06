package com.technotapp.servicestation.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;


public class InputDialogFragment extends DialogFragment implements View.OnClickListener {

    private EditText mETPassword;
    private OnInputDialogClick mOnInputDialogClick;

    public static InputDialogFragment newInstance() {
        InputDialogFragment fragment = new InputDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_input, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);
        mETPassword = rootView.findViewById(R.id.fragment_dialog_input_et_password);
        rootView.findViewById(R.id.fragment_dialog_input_btn_accept).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_dialog_input_btn_cancel).setOnClickListener(this);

        initView();

        return rootView;
    }

    private void initView() {
        try {

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(),e, "SuccessfulDialogFragment", "initView");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_dialog_input_btn_cancel) {
            dismiss();
        } else if (v.getId() == R.id.fragment_dialog_input_btn_accept) {
            mOnInputDialogClick.onAccept(mETPassword.getText().toString());
            dismiss();
        }
    }

    public void setOnInputDialogClickListener(OnInputDialogClick onInputDialogClick) {
        mOnInputDialogClick = onInputDialogClick;
    }

    public interface OnInputDialogClick {
        void onAccept(String password);
    }
}
