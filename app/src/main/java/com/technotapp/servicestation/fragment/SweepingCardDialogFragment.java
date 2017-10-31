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

import com.technotapp.servicestation.R;

public class SweepingCardDialogFragment extends DialogFragment implements View.OnClickListener {
    private Thread mCardSweepTimeoutThread;
    private Handler handler;
    private Button btn;
    private ISweepDialog mISweepDialog;
    private boolean mIsDialogHide = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_sweepingcard_progress, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        btn = (Button) view.findViewById(R.id.fragment_dialog_sweepingcard_progress_button);
        btn.setOnClickListener(this);
        counter((ProgressBar) view.findViewById(R.id.fragment_dialog_sweepingcard_progress_progressBar), 1000);
        return view;
    }


    @SuppressLint("HandlerLeak")
    private void counter(final ProgressBar progressBar, final int second) {

        mCardSweepTimeoutThread = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = second; i > 0; i--) {
                    try {
                        Thread.sleep(10);
                        Message message = Message.obtain();
                        message.arg1 = i / 10;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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

    }

    public void show(Activity activity, ISweepDialog iSweepDialog) {
        mISweepDialog = iSweepDialog;
        this.setCancelable(false);
        this.show(activity.getFragmentManager(), "sweepcard");
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = 600;
        int height = 300;
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_dialog_sweepingcard_progress_button) {
            mIsDialogHide = true;
            mISweepDialog.onCancelOrTimeout();
        }
    }

    public void intruptSweepCard() {
        mIsDialogHide = true;
        mCardSweepTimeoutThread.interrupt();
        mCardSweepTimeoutThread = null;
    }

    public interface ISweepDialog {
        void onCancelOrTimeout();
    }
}
