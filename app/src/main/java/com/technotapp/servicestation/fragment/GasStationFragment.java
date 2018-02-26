package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.activity.CustomQRScannerActivity;
import com.technotapp.servicestation.application.Constant;

import butterknife.ButterKnife;

public class GasStationFragment extends SubMenuFragment implements View.OnClickListener {

    private Bundle mBundle;

    public static GasStationFragment newInstance() {
        GasStationFragment fragment = new GasStationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rooView = inflater.inflate(R.layout.fragment_gas_station, container, false);
        initView(rooView);
        initAdapter();

        return rooView;
    }

    private void initAdapter() {

    }

    private void initView(View rooView) {
        ButterKnife.bind(rooView);
        setTitle("پمپ بنزین");
        mBundle = new Bundle();
        rooView.findViewById(R.id.fragment_gasstation_read).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fragment_gasstation_read) {
            readQRCode();
        }
    }

    private void readQRCode() {
        try {

            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
            integrator.setCaptureActivity(CustomQRScannerActivity.class);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("بارکد دوبعدی را اسکن کنید");
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "GasStationFragment", "readQRCode");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.RequestCode.QR_SCANNER:
                    IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    String codeContent = scanningResult.getContents();
                    String codeFormat = scanningResult.getFormatName();
                    if (codeContent != null && codeFormat != null && !TextUtils.isEmpty(codeContent) && !TextUtils.isEmpty(codeFormat)) {
                        switch (codeContent) {

                            case "E36EC6E3F9894D6313A91476544DDB1560C487A9888E0719988DE7C156CA9E2E":
                                mBundle.putString(Constant.Key.PAYMENT_AMOUNT, "360000");
                                mBundle.putString(Constant.Key.LITR, "36");
                                mBundle.putString(Constant.Key.DISPENSER, "نازل شماره 1");
                                break;
                            case "31085D0C586D343BCE7077842AE2472D422DE85CBC6886F9D25744DF782D6A0B":
                                mBundle.putString(Constant.Key.PAYMENT_AMOUNT, "450000");
                                mBundle.putString(Constant.Key.LITR, "45");
                                mBundle.putString(Constant.Key.DISPENSER, "نازل شماره 2");
                                break;
                        }


                        try {
                            GasStationConfirmFragment dialogFragment = GasStationConfirmFragment.newInstance();
                            dialogFragment.show(getActivity().getFragmentManager(), "");
                            dialogFragment.setCancelable(false);
                            dialogFragment.setArguments(mBundle);
                            //todo change this transaction
                        } catch (Exception e) {
                            AppMonitor.reportBug(getActivity(), e, "GasStationFragment", "submitFragment");
                        }
                    }
            }
        }
    }
}
