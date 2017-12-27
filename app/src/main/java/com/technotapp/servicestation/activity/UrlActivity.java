package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.MenuModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UrlActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    @BindView(R.id.toolbar_tv_title)
    TextView tv_title;
    private String mUrl;
    private WebView mBrowser;
    private MenuModel mMenuModel;

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
        if (mMenuModel != null && mMenuModel.description != null && !TextUtils.isEmpty(mMenuModel.description)) {
            tv_title.setText(mMenuModel.description);
        }

    }

    private void loadData() {
        mUrl = getIntent().getStringExtra(Constant.Key.ACTION_URL);
        long menuId = getIntent().getLongExtra(Constant.Key.MENU_ID, -1);
        mMenuModel = Db.Menu.getMenuById(menuId);

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
            Helper.progressBar.showDialog(UrlActivity.this, "در حال بارگزاری");
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
