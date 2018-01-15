package com.technotapp.servicestation.fragment;


import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AlertDialogFragment extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.fragment_dialog_alert_text)
    TextView textView;
    @BindView(R.id.fragment_dialog_alert_image)
    ImageView imageView;
    @BindView(R.id.fragment_dialog_alert_button)
    Button btn_confirm;
    @BindView(R.id.fragment_dialog_alert_layout)
    LinearLayout linearLayout;

    private Unbinder unbinder;
    String className;
    private int mAlertType;
    private String mMessage;
    private String mButtonText = "";

    public static AlertDialogFragment newInstance(int alertType, String message) {
        AlertDialogFragment fragment = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("alertType", alertType);
        args.putString("alertMessage", message);
        fragment.setArguments(args);
        return fragment;
    }

    public static AlertDialogFragment newInstance(int alertType, String message, String ButtonText) {
        AlertDialogFragment fragment = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("alertType", alertType);
        args.putString("alertMessage", message);
        args.putString("buttonText", ButtonText);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);
        }
        className = getClass().getSimpleName();
        View rootView = inflater.inflate(R.layout.fragment_dialog_alert, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        loadData();
        initView();
        return rootView;
    }

    private void loadData() {
        Bundle bundle = getArguments();
        mAlertType = bundle.getInt("alertType");
        mMessage = bundle.getString("alertMessage");
        mButtonText = bundle.getString("buttonText");


    }

    private void initView() {
        try {
            if (!TextUtils.isEmpty(mButtonText)) {
                btn_confirm.setText(mButtonText);
            }
            btn_confirm.setOnClickListener(this);
            if (mAlertType == Constant.AlertType.Information) {
                linearLayout.setBackgroundResource(R.drawable.bg_lyr_dialog_info);
                imageView.setBackgroundResource(R.drawable.ic_information);
                btn_confirm.setTextColor(getResources().getColor(R.color.white));
                btn_confirm.setBackgroundResource(R.drawable.bg_btn_info);
            } else if (mAlertType == Constant.AlertType.Warning) {

            } else if (mAlertType == Constant.AlertType.Error) {
                linearLayout.setBackgroundResource(R.drawable.bg_lyr_dialog_error);
                imageView.setBackgroundResource(R.drawable.ic_error_message);
                btn_confirm.setTextColor(getResources().getColor(R.color.error_dialog_color));
                btn_confirm.setBackgroundResource(R.drawable.bg_btn_white_raduce);

            } else if (mAlertType == Constant.AlertType.Success) {
                linearLayout.setBackgroundResource(R.drawable.bg_lyr_dialog_success);
                imageView.setBackgroundResource(R.drawable.ic_successfull);
                btn_confirm.setTextColor(getResources().getColor(R.color.success_dialog_color));
                btn_confirm.setBackgroundResource(R.drawable.bg_btn_white_raduce);
            }
            textView.setText(mMessage);
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, className, "initView");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_dialog_alert_button) {
            dismiss();
        }
    }

    public void setConfirmClickListener(View.OnClickListener clickListener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                btn_confirm.setOnClickListener(clickListener);

            }
        });
    }


    public void show(Activity activity) {
        try {
            setCancelable(false);
            show(activity.getFragmentManager(), "alert");
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, className, "show");
        }
    }
}
