package com.technotapp.servicestation.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;

public class SweepingCardDialogFragment extends DialogFragment implements View.OnClickListener {
    private Thread mCardSweepTimeoutThread;
    private Handler handler;
    private OnSweepDialogListener mISweepDialog;
    private boolean mIsDialogHide = false;
    private TextView txtCounter;
    private Button btnCancel;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_dialog_sweepingcard_progress, container);
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_btn_swipe_card);
            }
            initView(view);
            counter(progressBar, txtCounter);
            return view;

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "SweepingCardDialogFragment", "onCreateView");
            return null;
        }
    }

    private void initView(View v) {
        txtCounter = v.findViewById(R.id.fragment_dialog_sweepingcard_txtCounter);
        btnCancel = v.findViewById(R.id.fragment_dialog_sweepingcard_progress_btn_cancel);
        progressBar = v.findViewById(R.id.fragment_dialog_sweepingcard_progress_progressBar);

        btnCancel.setOnClickListener(this);
    }


    @SuppressLint("HandlerLeak")
    private void counter(final ProgressBar progressBar, final TextView txtCounter) {
        try {
            mCardSweepTimeoutThread = new Thread(new Runnable() {

                @Override
                public void run() {

                    for (int i = 1000; i > 0; i--) {
                        try {
                            Thread.sleep(10);
                            Message message = Message.obtain();
                            message.arg1 = i / 10;
                            message.arg2 = i / 100;
                            handler.sendMessage(message);
                        } catch (InterruptedException e) {
                            AppMonitor.reportBug(getActivity(), e, "SweepingCardDialogFragment", "counter:for");
                        }
                    }
                    if (mIsDialogHide) {
                        return;
                    }
                    mISweepDialog.onCancelOrTimeout();
                }
            });

            mCardSweepTimeoutThread.start();
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    progressBar.setProgress(msg.arg1);
                    txtCounter.setText((msg.arg2 + 1) + "");

                }
            };
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "SweepingCardDialogFragment", "counter");

        }


    }

    public void show(Activity activity, OnSweepDialogListener iSweepDialog) {
        try {

            mISweepDialog = iSweepDialog;
            this.setCancelable(false);
            this.show(activity.getFragmentManager(), "sweepcard");
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "SweepingCardDialogFragment", "show");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        int width = 600;
//        int height = 300;
        try {
//            if (getDialog().getWindow() != null) {
//                getDialog().getWindow().setLayout(width, height);
//            }

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "SweepingCardDialogFragment", "onResume");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_dialog_sweepingcard_progress_btn_cancel) {
            try {
                mIsDialogHide = true;
                mISweepDialog.onCancelOrTimeout();
            } catch (Exception e) {
                AppMonitor.reportBug(getActivity(), e, "SweepingCardDialogFragment", "onClick");
            }
        }
    }

    public void interruptSweepCard() {
        mIsDialogHide = true;
        mCardSweepTimeoutThread.interrupt();
        mCardSweepTimeoutThread = null;
    }

    public interface OnSweepDialogListener {
        void onCancelOrTimeout();
    }
}
