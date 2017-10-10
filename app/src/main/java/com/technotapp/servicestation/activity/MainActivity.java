package com.technotapp.servicestation.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.technotapp.servicestation.R;
import com.technotapp.servicestation.pax.mag.IMagCardCallback;
import com.technotapp.servicestation.pax.mag.MagCard;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void run(View v) {
        MagCard magCard = MagCard.getInstance();
        magCard.start(this, new IMagCardCallback() {
            @Override
            public void onFail() {
            }

            @Override
            public void onSuccessful(String track1, String track2, String track3) {
            }
        });
    }
}
