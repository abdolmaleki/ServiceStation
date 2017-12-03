package com.technotapp.servicestation.fragment;


import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    Button button;
    @BindView(R.id.fragment_dialog_alert_layout)
    LinearLayout linearLayout;

    private Unbinder unbinder;
    String className;
    private int mAlertType;
    private String mMessage;

    public static AlertDialogFragment newInstance(int alertType, String message) {
        AlertDialogFragment fragment = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("alertType", alertType);
        args.putString("alertMessage", message);
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

    }

    private void initView() {
        try {
            button.setOnClickListener(this);
            if (mAlertType == Constant.AlertType.Information) {
                linearLayout.setBackgroundResource(R.drawable.bg_lyr_dialog_info);
                imageView.setBackgroundResource(R.drawable.ic_information);
                button.setTextColor(getResources().getColor(R.color.white));
                button.setBackgroundResource(R.drawable.bg_btn_info);
            } else if (mAlertType == Constant.AlertType.Warning) {

            } else if (mAlertType == Constant.AlertType.Error) {
                linearLayout.setBackgroundResource(R.drawable.bg_lyr_dialog_error);
                imageView.setBackgroundResource(R.drawable.ic_error_message);
                button.setTextColor(getResources().getColor(R.color.error_color));
                button.setBackgroundResource(R.drawable.bg_btn_error_success);

            } else if (mAlertType == Constant.AlertType.Success) {
                linearLayout.setBackgroundResource(R.drawable.bg_lyr_dialog_success);
                imageView.setBackgroundResource(R.drawable.ic_successfull);
                button.setTextColor(getResources().getColor(R.color.success_dialog_color));
                button.setBackgroundResource(R.drawable.bg_btn_error_success);
            }
            textView.setText(mMessage);
        } catch (Exception e) {
            AppMonitor.reportBug(e, className, "initView");
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

    public void show(Activity activity) {
        try {
            setCancelable(false);
            show(activity.getFragmentManager(), "alert");
        } catch (Exception e) {
            AppMonitor.reportBug(e, className, "show");
        }
    }
}
