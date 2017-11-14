package com.technotapp.servicestation.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pax.dal.IScanner;
import com.pax.dal.entity.EScannerType;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ReceiptFragment extends SubMenuFragment implements View.OnClickListener {

    @BindView(R.id.fragment_receipt_btnQrReader)
    ImageButton btnQrReader;
    @BindView(R.id.fragment_receipt_edtPaymentCode)
    EditText edtPaymentCode;
    @BindView(R.id.fragment_receipt_edtBillingId)
    EditText edtBillingId;

    private Unbinder unbinder;

    public static ReceiptFragment newInstance() {
        ReceiptFragment fragment = new ReceiptFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_receipt, container, false);
        ButterKnife.bind(this, rootView);
        unbinder = ButterKnife.bind(this, rootView);

        initView();

        return rootView;
    }

    private void initView() {
        try {
            setRetainInstance(true);

            setTitle(getString(R.string.ReceiptFragment_billingPay));

            btnQrReader.setOnClickListener(this);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ReceiptFragment", "initView");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private NeptuneLiteUser neptuneLiteUser = NeptuneLiteUser.getInstance();

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_receipt_btnQrReader) {
            try {
                neptuneLiteUser.getDal(getActivity()).getScanner(EScannerType.RIGHT).open();
                neptuneLiteUser.getDal(getActivity()).getScanner(EScannerType.RIGHT).start(new IScanner.IScanListener() {
                    @Override
                    public void onRead(String result) {
                        if (result != null) {
                            if (result.length() == 26) {
                                parseBillDetail(result);

                            } else {

                            }
                        } else {
                            AppMonitor.Log("result null");

                        }
                    }

                    @Override
                    public void onFinish() {
                        try {
                            neptuneLiteUser.getDal(getActivity()).getScanner(EScannerType.RIGHT).close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancel() {

                    }
                });

            } catch (Exception e) {
                AppMonitor.reportBug(e, "ReceiptFragment", "submitFragment");
            }

        }
    }


    private void parseBillDetail(String strBarcode) {
        String paymentCode = strBarcode.substring(0, 13);
        String billingId = strBarcode.substring(14);
        String amount = Integer.parseInt(billingId.substring(0, 7)) * 1000 + "";
        byte organizationCode = Byte.parseByte(paymentCode.substring(11, 12));
        if (organizationCode==0){

        }else {
            edtPaymentCode.setText(paymentCode);
            edtBillingId.setText(billingId);
        }
    }

    private int getImageOrganization(byte organizationCode) {
        switch (organizationCode) {
            case Constant.PayBill.Organization.WATER:
                return R.drawable.ic_organization_water;
            case Constant.PayBill.Organization.ELECTRICAL:
                return R.drawable.ic_organization_electrical;
            case Constant.PayBill.Organization.GAS:
                return R.drawable.ic_organization_gas;
            case Constant.PayBill.Organization.PHONE:
                return R.drawable.ic_organization_phone;
            case Constant.PayBill.Organization.MOBILE:
                return R.drawable.ic_organization_phone;
            case Constant.PayBill.Organization.TAX_OF_MUNICIPALITY:
                return R.drawable.ic_organization_tax_of_municipality;
            case Constant.PayBill.Organization.TAX:
                return R.drawable.ic_organization_tax;
            case Constant.PayBill.Organization.TRAFFIC_CRIME:
                return R.drawable.ic_organization_traffic_police;
            default:
                return 0;
        }
    }
}
