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
import android.widget.ProgressBar;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;

public class SweepingCardDialogFragment extends DialogFragment implements View.OnClickListener {
    private Thread mCardSweepTimeoutThread;
    private Handler handler;
    private ISweepDialog mISweepDialog;
    private boolean mIsDialogHide = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        try {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_btn_swipe_card);
            View view = inflater.inflate(R.layout.fragment_dialog_sweepingcard_progress, container);
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            }
            (view.findViewById(R.id.fragment_dialog_sweepingcard_progress_button)).setOnClickListener(this);
            counter((ProgressBar) view.findViewById(R.id.fragment_dialog_sweepingcard_progress_progressBar));
            return view;

        } catch (Exception e) {
            AppMonitor.reportBug(e, "SweepingCardDialogFragment", "onCreateView");
            return null;
        }
    }


    @SuppressLint("HandlerLeak")
    private void counter(final ProgressBar progressBar) {
        try {
            mCardSweepTimeoutThread = new Thread(new Runnable()  {

                @Override
                public void run() {

                    for (int i = 1000; i > 0; i--) {
                        try {
                            Thread.sleep(10);
                            Message message = Message.obtain();
                            message.arg1 = i / 10;
                            handler.sendMessage(message);
                        } catch (InterruptedException e) {
                            AppMonitor.reportBug(e, "SweepingCardDialogFragment", "counter:for");
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

                }
            };
        } catch (Exception e) {
            AppMonitor.reportBug(e, "SweepingCardDialogFragment", "counter");

        }


    }

    public void show(Activity activity, ISweepDialog iSweepDialog) {
        try {

            mISweepDialog = iSweepDialog;
            this.setCancelable(false);
            this.show(activity.getFragmentManager(), "sweepcard");
        } catch (Exception e) {
            AppMonitor.reportBug(e, "SweepingCardDialogFragment", "show");
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
            AppMonitor.reportBug(e, "SweepingCardDialogFragment", "onResume");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_dialog_sweepingcard_progress_button) {
            try {

                mIsDialogHide = true;
                mISweepDialog.onCancelOrTimeout();
            } catch (Exception e) {
                AppMonitor.reportBug(e, "SweepingCardDialogFragment", "onClick");
            }
        }
    }

    public void interruptSweepCard() {
        mIsDialogHide = true;
        mCardSweepTimeoutThread.interrupt();
        mCardSweepTimeoutThread = null;
    }

    public interface ISweepDialog {
        void onCancelOrTimeout();
    }
}
