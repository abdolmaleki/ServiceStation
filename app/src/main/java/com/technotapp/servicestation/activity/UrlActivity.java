package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UrlActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    private String mUrl;
    private WebView mBrowser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);
        loadData();
        initView();
        loadPage();
    }

    private void initView() {
        ButterKnife.bind(this);
        mBrowser = findViewById(R.id.activity_url_wv_browser);
        back.setOnClickListener(this);

    }

    private void loadData() {
        mUrl = getIntent().getStringExtra(Constant.Key.ACTION_URL);
    }

    private void adjustBrowser() {

        try {
            mBrowser.getSettings().setLoadsImagesAutomatically(true);
            mBrowser.getSettings().setJavaScriptEnabled(true);
            mBrowser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            mBrowser.setWebViewClient(new BrowserClient());

        } catch (Exception e) {
            AppMonitor.reportBug(e, "UrlActivity", "adjustBrowser");

        }

    }

    private void loadPage() {
        try {
            Helper.progressBar.showDialog(UrlActivity.this,"در حال بارگزاری");
            adjustBrowser();
            mBrowser.loadUrl(mUrl);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "UrlActivity", "loadPage");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.toolbar_img_back:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    private class BrowserClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            Helper.progressBar.hideDialog();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Helper.progressBar.hideDialog();
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            Helper.progressBar.hideDialog();
        }
    }
}
