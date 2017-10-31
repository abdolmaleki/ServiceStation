package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.technotapp.servicestation.R;


public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSignin;
    private EditText edtUsername;
    private EditText edtPassword;
    private String fakeUsername = "0000";
    private String fakePassword = "0000";

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
        btnSignin = (Button) findViewById(R.id.activity_signin_btnSignin);
        edtUsername = (EditText) findViewById(R.id.activity_signin_edtMerchantCode);
        edtPassword = (EditText) findViewById(R.id.activity_signin_edtPassword);

    }

    private void clickHandler() {
        btnSignin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_signin_btnSignin:
                if (checkAuthentication(edtUsername, edtPassword)) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                break;
        }
    }

    private boolean checkAuthentication(EditText username, EditText password) {
        //todo remove hardcode
        if (username.getText().toString().isEmpty()) {
            username.setError("لطفا کد بازرگان را وارد کنید");
            return false;
        } else if (password.getText().toString().isEmpty()) {
            password.setError("لطفا رمز عبور را را وارد کنید");
            return false;
        } else if (!username.getText().toString().trim().equals(fakeUsername)) {
            Toast.makeText(this, "اطلاعات وارد شده اشتباه است", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.getText().toString().trim().equals(fakePassword)) {
            Toast.makeText(this, "اطلاعات وارد شده اشتباه است", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
