package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.fragment.ExitConfirmDialog;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {

        ExitConfirmDialog exitConfirmDialog = ExitConfirmDialog.newInstance();
        exitConfirmDialog.show(getFragmentManager(), "exit");
        exitConfirmDialog.setOnDialogClick(new ExitConfirmDialog.OnExitListener() {
            @Override
            public void onAccept() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constant.Key.EXIT, true);
                startActivity(intent);
                exitConfirmDialog.dismiss();
                finish();
            }
        });
    }
}
