package com.technotapp.servicestation.fragment;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.TransactionHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.dto.TerminalTransactionDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseTransactionSto;
import com.technotapp.servicestation.connection.restapi.sto.BillPaymentSto;
import com.technotapp.servicestation.enums.ServiceType;
import com.technotapp.servicestation.pax.printer.PrintMaker;
import com.technotapp.servicestation.setting.Session;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class GasStationConfirmFragment extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.fragment_gas_station_tv_dispenser)
    TextView txtDispenserNumber;
    @BindView(R.id.fragment_gas_station_tv_litr)
    TextView txtLitr;
    @BindView(R.id.fragment_gas_station_tv_amount)
    TextView txtAmount;
    @BindView(R.id.fragment_gas_station_btn_back)
    Button btnBack;

    Session mSession;

    private String mAmount;
    private String mDispenserNnumber;
    private String mLitr;
    private Unbinder unbinder;

    public static GasStationConfirmFragment newInstance() {
        GasStationConfirmFragment fragment = new GasStationConfirmFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_dialog_gasstation_confirm, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {
        try {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                mAmount = bundle.getString(Constant.Key.PAYMENT_AMOUNT);
                mLitr = bundle.getString(Constant.Key.LITR);
                mDispenserNnumber = bundle.getString(Constant.Key.DISPENSER);
            }
            txtLitr.setText(mLitr);
            String formatted = String.format(Locale.CANADA, "%,d", Long.parseLong(mAmount));
            txtAmount.setText(formatted + "  ریال");
            txtDispenserNumber.setText(mDispenserNnumber);
            btnBack.setOnClickListener(this);
            rootView.findViewById(R.id.fragment_gas_station_tv_amount_btn_confirm).setOnClickListener(this);


            mSession = Session.getInstance(getActivity());

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "GasStationConfirmFragment", "initView");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_gas_station_tv_amount_btn_confirm) {
            TransactionHelper.startServiceTransaction(getActivity(), ServiceType.BUY_PRODUCT, createDto(), new PaymentListFragment.PaymentResultListener() {
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
        } else if (v.getId() == R.id.fragment_gas_station_btn_back) {
            dismiss();
        }


    }

    private TerminalTransactionDto createDto() {
        TerminalTransactionDto dto = new TerminalTransactionDto();
        dto.transactionModel.amountOfTransaction = Long.parseLong(mAmount);

        return dto;
    }


}
