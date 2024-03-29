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
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.TransactionHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.dto.BillPaymentDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseTransactionSto;
import com.technotapp.servicestation.connection.restapi.sto.BillPaymentSto;
import com.technotapp.servicestation.enums.ServiceType;
import com.technotapp.servicestation.pax.printer.PrintMaker;
import com.technotapp.servicestation.setting.Session;

import java.util.Locale;
import java.util.UUID;

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

    Session mSession;

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

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {
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
            txtAmount.setText(formatted + "  ریال");
            txtPaymentCode.setText(paymentCode);
            imgOrganization.setImageResource(organizationImage);
            txtOrganization.setText(organizationName);
            btnBack.setOnClickListener(this);
            rootView.findViewById(R.id.fragment_receipt_end_btnConfirm).setOnClickListener(this);


            mSession = Session.getInstance(getActivity());

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "ReceiptFragment", "initView");
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
            TransactionHelper.startServiceTransaction(getActivity(), ServiceType.BILL, createDto(), new PaymentListFragment.PaymentResultListener() {
                @Override
                public void onSuccessfullPayment(String message, BaseTransactionSto response) {
                    Helper.alert(getActivity(), message, Constant.AlertType.Success);
                    PrintMaker.startFactorPrint(getActivity(), response);
                    dismiss();
                }

                @Override
                public void onFailedPayment(String message, BaseTransactionSto baseTransactionSto) {


                    Helper.alert(getActivity(), message, Constant.AlertType.Error);
                    BillPaymentSto sto = (BillPaymentSto) baseTransactionSto;
                    if (sto != null) {
                        if (sto.errorCode.equals(String.valueOf(Constant.Api.ErrorCode.Successfull)) || sto.errorCode.equals(Constant.Api.TransactionErrorCode.Successfull)) { //successfulTransaction
                            // PrintMaker.failOperationPrint(getActivity(), baseTransactionSto);
                        }
                    }
                    dismiss();
                }

                @Override
                public void onCancel() {

                }
            });
        } else if (v.getId() == R.id.fragment_receipt_end_btnBack) {
            dismiss();
        }


    }

    private BillPaymentDto createDto() {
        BillPaymentDto dto = new BillPaymentDto();
        dto.tokenId = mSession.getTokenId();
        dto.terminalCode = mSession.getTerminalId();
        dto.billModel.amount = Long.parseLong(amount);
        dto.billModel.billingId = billingId;
        dto.billModel.paymentId = paymentCode;
        dto.billModel.userOrderId = UUID.randomUUID().toString();

        return dto;
    }


}
