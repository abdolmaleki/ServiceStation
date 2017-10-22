package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.technotapp.servicestation.R;


public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        loadSetting();

        loadData();

        bindView();

        clickHandler();


    }

    private void loadSetting() {

    }

    private void loadData() {

    }

    private void bindView() {
        btn = (Button) findViewById(R.id.activity_signin_btnSignin);
    }

    private void clickHandler() {
        btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_signin_btnSignin:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

}
