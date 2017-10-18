package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.fujiyuu75.sequent.Animation;
import com.fujiyuu75.sequent.Sequent;
import com.technotapp.servicestation.Infrastructure.NetworkHelper;
import com.technotapp.servicestation.R;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {
    LinearLayout logoLayout;
    LinearLayout textLayout;

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadSetting();

        loadData();

        initView();

        playSplash();

    }

    private void playSplash() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);

                    }
                });
                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus / 10);
                        }
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (NetworkHelper.isNetworkAvailable(SplashActivity.this)) {
                    startActivity(new Intent(SplashActivity.this, SigninActivity.class));

                } else {
                    startActivity(new Intent(SplashActivity.this, CheckNetworkActivity.class));

                }
                finish();
            }
        }, 4000);
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.activity_splash_prg);
        logoLayout = (LinearLayout) findViewById(R.id.activity_splash_logoLayout);
        textLayout = (LinearLayout) findViewById(R.id.activity_splash_textLayout);

        Sequent.origin(logoLayout).
                duration(1000).
                anim(this, Animation.FADE_IN_UP).
                start();

        Sequent.origin(textLayout).
                delay(1000).
                duration(2000).
                anim(this, Animation.FADE_IN).
                start();

    }

    private void loadData() {

    }

    private void loadSetting() {

    }
}
