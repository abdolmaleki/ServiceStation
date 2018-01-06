package com.technotapp.servicestation.fragment;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ReceiptDialogFragment extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.fragment_receipt_end_txtBillingId)
    TextView txtBillingId;
    @BindView(R.id.fragment_receipt_end_txtPaymentCode)
    TextView txtPaymentCode;
    @BindView(R.id.fragment_receipt_end_txtAmount)
    TextView txtAmount;
    @BindView(R.id.fragment_receipt_end_txtOrganization)
    TextView txtOrganization;
    @BindView(R.id.fragment_receipt_end_imgOrganization)
    ImageView imgOrganization;
    @BindView(R.id.fragment_receipt_end_btnBack)
    Button btnBack;

    private int organizationImage;
    private String amount;
    private String paymentCode;
    private String billingId;
    private String organizationName;
    private Unbinder unbinder;

    public static ReceiptDialogFragment newInstance() {
        ReceiptDialogFragment fragment = new ReceiptDialogFragment();
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
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);
        View rootView = inflater.inflate(R.layout.fragment_dialog_receipt, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        initView();

        return rootView;
    }

    private void initView() {
        try {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                organizationImage = bundle.getInt("organization_image");
                amount = bundle.getString("amount");
                paymentCode = bundle.getString("paymentCode");
                billingId = bundle.getString("billingId");
                organizationName = bundle.getString("organization_name");
            }
            txtBillingId.setText(billingId);
            String formatted = String.format(Locale.CANADA, "%,d", Long.parseLong(amount));
            txtAmount.setText(formatted+"  ریال");
            txtPaymentCode.setText(paymentCode);
            imgOrganization.setImageResource(organizationImage);
            txtOrganization.setText(organizationName);
            btnBack.setOnClickListener(this);

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(),e, "ReceiptFragment", "initView");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_receipt_end_btnConfirm) {

        }
        else if (v.getId() == R.id.fragment_receipt_end_btnBack){
            dismiss();
        }


    }


}
