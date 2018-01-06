package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;

public class PrintDialogFragment extends DialogFragment implements View.OnClickListener {
    private Button mBtnConfirm;
    private Bitmap mBitmap;
    private ImageView mTransactionContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDate();
    }

    private void loadDate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mBitmap = bundle.getParcelable(Constant.Key.PRINT_BITMAP);
        }
    }

    public static PrintDialogFragment newInstance(Bitmap bitmap) {
        PrintDialogFragment fragment = new PrintDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.Key.PRINT_BITMAP, bitmap);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_dialog_print, container);
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            }
            initView(view);
            return view;

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(),e, "PrintDialogFragment", "onCreateView");
            return null;
        }
    }


    private void initView(View v) {
        mBtnConfirm = v.findViewById(R.id.fragment_dialog_btn_seen);
        mBtnConfirm.setOnClickListener(this);
        mTransactionContent = v.findViewById(R.id.fragment_dialog_iv_print_content);

        if (mBitmap != null) {
            mTransactionContent.setImageBitmap(mBitmap);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT );

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(),e, "PrintDialogFragment", "onResume");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_dialog_btn_seen) {
            dismiss();
        }
    }

    public void show(Activity activity) {
        try {
            setCancelable(false);
            show(activity.getFragmentManager(), "alert");
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(),e, "PrintDialogFragment", "show");

        }
    }
}
