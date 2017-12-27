package com.technotapp.servicestation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.technotapp.servicestation.activity.CustomScannerActivity;

public class ScannerFragment extends Fragment {

    private String codeContent;
    private String codeFormat;
    private OnScanListener mOnScanListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("اسکنر را روی بارکد قرار دهید");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        codeContent = scanningResult.getContents();
        codeFormat = scanningResult.getFormatName();
        if (codeContent != null && codeFormat != null && !TextUtils.isEmpty(codeContent) && !TextUtils.isEmpty(codeFormat)) {
            mOnScanListener.onSuccess(codeContent, codeFormat);
        } else {
            mOnScanListener.onFailed();
        }
    }

    public void setOnScanListener(OnScanListener onScanListener) {
        mOnScanListener = onScanListener;
    }

    public interface OnScanListener {
        void onSuccess(String codeContent, String codeFormat);

        void onFailed();
    }


}
