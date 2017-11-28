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
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SuccessfulDialogFragment extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.fragment_dialog_success_text)
    TextView textView;
    @BindView(R.id.fragment_dialog_success_button)
    Button button;
    String message;
    private Unbinder unbinder;

    public static SuccessfulDialogFragment newInstance() {
        SuccessfulDialogFragment fragment = new SuccessfulDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);
        View rootView = inflater.inflate(R.layout.fragment_dialog_successful, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        initView();

        return rootView;
    }

    private void initView() {
        try {
            button.setOnClickListener(this);
            textView.setText(message);

        } catch (Exception e) {
            AppMonitor.reportBug(e, "SuccessfulDialogFragment", "initView");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_dialog_success_button) {
            dismiss();
        }
    }

    public void show(Activity activity, String message) {
        try {
            setCancelable(false);
            show(activity.getFragmentManager(), "successful");
            this.message = message;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "SuccessfulDialogFragment", "show");
        }
    }
}
