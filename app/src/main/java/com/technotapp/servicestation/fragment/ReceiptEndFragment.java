package com.technotapp.servicestation.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ReceiptEndFragment extends SubMenuFragment implements View.OnClickListener {
    @BindView(R.id.fragment_receipt_end_txtBillingId)
    TextView txtBillingId;
    @BindView(R.id.fragment_receipt_end_txtPaymentCode)
    TextView txtPaymentCode;
    @BindView(R.id.fragment_receipt_end_txtAmount)
    TextView txtAmount;
    @BindView(R.id.fragment_receipt_end_imgOrganization)
    ImageView imgOrganization;

    int OrganizationImage;
    String amount;
    String paymentCode;
    String billingId;
    private Unbinder unbinder;

    public static ReceiptEndFragment newInstance() {
        ReceiptEndFragment fragment = new ReceiptEndFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_receipt_end, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        initView();

        return rootView;
    }

    private void initView() {
        try {
            setRetainInstance(true);

            setTitle(getString(R.string.ReceiptFragment_billingPay));

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                OrganizationImage = bundle.getInt("organization_image");
                amount = bundle.getString("amount");
                paymentCode = bundle.getString("paymentCode");
                billingId = bundle.getString("billingId");
            }
            txtBillingId.setText(billingId);
            txtAmount.setText(amount);
            txtPaymentCode.setText(paymentCode);
            imgOrganization.setImageResource(OrganizationImage);

//            btnQrReader.setOnClickListener(this);

        } catch (Exception e) {
            AppMonitor.reportBug(e, "ReceiptFragment", "initView");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_receipt_btnQrReader) {

        }


    }


}
