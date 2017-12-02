package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;

public class LoadingDialogFragment extends DialogFragment{
    private TextView textView;
    private ImageView imageView;
    String message;

    public static LoadingDialogFragment newInstance() {
        LoadingDialogFragment fragment = new LoadingDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        try {
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);
            }
            View view = inflater.inflate(R.layout.fragment_dialog_loading, container);
            initView(view);
            return view;

        } catch (Exception e) {
            AppMonitor.reportBug(e, "SweepingCardDialogFragment", "onCreateView");
            return null;
        }
    }

    private void initView(View v) {
        textView = v.findViewById(R.id.fragment_dialog_loading_textView);
        imageView = v.findViewById(R.id.fragment_dialog_loading_imgLoading);
        Glide.with(getActivity()).load(R.drawable.ic_loading).into(new DrawableImageViewTarget(imageView));
        textView.setText(message);
    }


    public void show(Activity activity, String message) {
        try {
            this.setCancelable(false);
            this.show(activity.getFragmentManager(), "loading");
            this.message = message;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "LoadingDialogFragment", "show");
        }
    }


}
