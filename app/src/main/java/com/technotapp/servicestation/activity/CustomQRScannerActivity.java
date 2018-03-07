package com.technotapp.servicestation.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;

public class CustomQRScannerActivity extends BaseActivity implements
        DecoratedBarcodeView.TorchListener, View.OnClickListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_qr_scanner);

        try {
            barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
            barcodeScannerView.setTorchListener(this);
            capture = new CaptureManager(this, barcodeScannerView);
            capture.initializeFromIntent(getIntent(), savedInstanceState);
            capture.decode();
            initView();
        } catch (Exception e) {
            AppMonitor.reportBug(this, e, "CustomQRScannerActivity", "onCreate");
        }
    }

    private void initView() {

        findViewById(R.id.activity_custom_scanner_btn_cancel).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    @Override
    public void onTorchOn() {
    }

    @Override
    public void onTorchOff() {
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.activity_custom_scanner_btn_cancel) {
            finish();
        }
    }
}
