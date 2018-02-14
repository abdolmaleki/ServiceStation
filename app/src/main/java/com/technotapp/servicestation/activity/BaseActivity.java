package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import com.technotapp.servicestation.setting.Session;


public class BaseActivity extends AppCompatActivity {

    private Session mSession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if (!authorization()) {
            startActivity(new Intent(this, SigninActivity.class));
            finish();
        }
    }

    private void init() {
        mSession = Session.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //PaxHelper.disableAllNavigationButton(this);
        if (!authorization()) {
            startActivity(new Intent(this, SigninActivity.class));
            finish();
        }

    }

    private boolean authorization() {
        boolean isAuthorized = true;

        String tokenId = mSession.getTokenId();
        String terminalId = mSession.getTerminalId();
        String merchantId = mSession.getMerchantId();

        if (tokenId == null || TextUtils.isEmpty(tokenId)) {
            isAuthorized = false;
        }

        if (terminalId == null || TextUtils.isEmpty(terminalId)) {
            isAuthorized = false;
        }

        if (merchantId == null || TextUtils.isEmpty(merchantId)) {
            isAuthorized = false;
        }
        return isAuthorized;
    }

    @Override
    public void onBackPressed() {
    }
}
