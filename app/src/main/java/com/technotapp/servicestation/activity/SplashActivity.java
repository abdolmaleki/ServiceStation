package com.technotapp.servicestation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.fujiyuu75.sequent.Animation;
import com.fujiyuu75.sequent.Sequent;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.NetworkHelper;
import com.technotapp.servicestation.Infrastructure.PaxHelper;
import com.technotapp.servicestation.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.activity_splash_prg)
    ProgressBar progressBar;
    @BindView(R.id.activity_splash_logoLayout)
    LinearLayout logoLayout;

    private int progressStatus = 0;
    private Handler handler;
    private final String mClassName = getClass().getSimpleName();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadSetting();

        loadData();

        initView();

        checkNetStatus();


    }

    //run splash animation and show progressBar after two second
    private void playSplash() {
        try {


            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    Looper.prepare();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);

                        }
                    });
                    while (progressStatus < 200) {
                        progressStatus += 1;
                        handler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress(progressStatus);
                            }
                        });
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            AppMonitor.reportBug(e, mClassName, "playSplash-Timer");
                        }
                    }
                    startActivity(new Intent(mContext, SigninActivity.class));
                    finish();
                }
            }, 2000);
        } catch (Exception e) {
            AppMonitor.reportBug(e, mClassName, "playSplash");
        }
    }

    //initialize views & variables
    private void initView() {
        try {
            ButterKnife.bind(this);
            mContext = SplashActivity.this;
            handler = new Handler(getMainLooper());
            Sequent.origin(logoLayout).
                    duration(1000).
                    anim(mContext, Animation.FADE_IN_UP).
                    start();
        } catch (Exception e) {
            AppMonitor.reportBug(e, mClassName, "initView");
        }
    }

    private void loadData() {

    }

    private void loadSetting() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        PaxHelper.disableAllNavigationButton(mContext);
    }

    private void checkNetStatus() {

        NetworkHelper.isConnectingToInternet(mContext, new NetworkHelper.CheckNetworkStateListener() {
            @Override
            public void onNetworkChecked(boolean isSuccess, String message) {
                if (isSuccess) {
                    playSplash();
                } else {
                    startActivity(new Intent(mContext, CheckNetworkActivity.class));
                }
            }
        });

    }
}
