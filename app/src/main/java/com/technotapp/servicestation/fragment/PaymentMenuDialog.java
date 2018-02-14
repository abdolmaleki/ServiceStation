package com.technotapp.servicestation.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.TransactionHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.activity.PaymentGatewayActivity;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.FactorModel;
import com.technotapp.servicestation.enums.PaymentType;
import com.technotapp.servicestation.pax.mag.IMagCardCallback;
import com.technotapp.servicestation.pax.mag.MagCard;
import com.technotapp.servicestation.setting.Session;

import java.util.ArrayList;


public class PaymentMenuDialog extends DialogFragment implements View.OnClickListener, IMagCard {

    private FactorModel mFactorModel;
    private Session mSession;
    private TransactionDataModel transactionDataModel;
    private PaymentResultListener mPaymentResultListener;
    private ArrayList<String> mPaymentTypeList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            long factorId = bundle.getLong(Constant.Key.FACTOR_ID, -1);
            if (factorId != -1) {
                mFactorModel = Db.Factor.getFactorById(factorId);
            }
            mPaymentTypeList = bundle.getStringArrayList(Constant.Key.PAYMENT_TYPE_LIST);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_dialog_payment_menu, container);
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_btn_swipe_card);
            }
            initView(view);
            return view;

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "PaymentMenuDialog", "onCreateView");
            return null;
        }
    }

    private void initView(View view) {

        mSession = Session.getInstance(getActivity());

        Button mBTN_Cash = view.findViewById(R.id.fragment_dialog_payment_menu_btn_cash);
        Button mBTN_Ewallet = view.findViewById(R.id.fragment_dialog_payment_menu_btn_ewallet);
        Button mBTN_Shetabi = view.findViewById(R.id.fragment_dialog_payment_menu_btn_shetabi);
        Button mBTN_QRCode = view.findViewById(R.id.fragment_dialog_payment_menu_btn_qrcode);

        mBTN_Cash.setOnClickListener(this);
        mBTN_Ewallet.setOnClickListener(this);
        mBTN_Shetabi.setOnClickListener(this);
        mBTN_QRCode.setOnClickListener(this);


        for (String paymentType : mPaymentTypeList) {

            switch (paymentType) {

                case PaymentType.CASH:
                    mBTN_Cash.setEnabled(true);
                    mBTN_Cash.setFocusable(true);
                    mBTN_Cash.setTextColor(getResources().getColor(R.color.purple));
                    break;

                case PaymentType.WALLET:
                    mBTN_Ewallet.setEnabled(true);
                    mBTN_Ewallet.setFocusable(true);
                    mBTN_Ewallet.setTextColor(getResources().getColor(R.color.purple));
                    break;

                case PaymentType.SHETABI:
                    mBTN_Shetabi.setEnabled(true);
                    mBTN_Shetabi.setFocusable(true);
                    mBTN_Shetabi.setTextColor(getResources().getColor(R.color.purple));
                    break;

                case PaymentType.QRCode:
                    mBTN_QRCode.setEnabled(true);
                    mBTN_QRCode.setFocusable(true);
                    mBTN_QRCode.setTextColor(getResources().getColor(R.color.purple));
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fragment_dialog_payment_menu_btn_cash:
                cashPayment(mFactorModel.getTotalPrice());
                break;
            case R.id.fragment_dialog_payment_menu_btn_ewallet:
                ewalletPayment();
                break;

            case R.id.fragment_dialog_payment_menu_btn_shetabi:
                startActivity(new Intent(getActivity(), PaymentGatewayActivity.class));
                break;
        }
    }

    private void ewalletPayment() {
        try {
            MagCard magCard = MagCard.getInstance();
            magCard.start(getActivity(), new IMagCardCallback() {
                @Override
                public void onFail() {

                }

                @Override
                public void onSuccessful(String track1, String track2, String track3) {
                    transactionDataModel = new TransactionDataModel();
                    transactionDataModel.setTerminalID(mSession.getTerminalId());

                    if (track1 != null && !track1.equals("")) {
                        transactionDataModel.setPanNumber(track1.substring(1, 17));
                    } else if (track2 != null && !track2.equals("")) {
                        transactionDataModel.setPanNumber(track2.substring(0, 16));
                    }
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "PaymentMenuDialog", "ewalletPayment");
        }
    }

    private void cashPayment(long factorTotalPrice) {
        try {
            CashPaymentConfirmDialog cashPaymentConfirmDialog = CashPaymentConfirmDialog.newInstance(factorTotalPrice);
            cashPaymentConfirmDialog.show(getActivity().getFragmentManager(), "cashPaymentConfirmDialog");
            cashPaymentConfirmDialog.setmOnCashPaymentDialogClick(new CashPaymentConfirmDialog.OnCashPaymentDialogClick() {
                @Override
                public void onAccept() {
                    mPaymentResultListener.onSuccessfullPayment(PaymentType.CASH, transactionDataModel);
                    cashPaymentConfirmDialog.dismiss();
                    closeDialog();

                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "PaymentMenuDialog", "cashPayment");
        }

    }

    @Override
    public void onPinEnteredSuccessfully() {

        try {
            TransactionHelper.sendRequest(getActivity(), Constant.RequestMode.BUY, transactionDataModel, String.valueOf(


                    mFactorModel.getTotalPrice()), new TransactionHelper.TransactionResultListener() {
                @Override
                public void onSuccessfullTransaction(TransactionDataModel transactionDataModel) {
                    mPaymentResultListener.onSuccessfullPayment(PaymentType.WALLET, transactionDataModel);
                }

                @Override
                public void onFailTransaction(String message) {
                    mPaymentResultListener.onFailedPayment(message);
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "PaymentMenuDialog", "onPinEnteredSuccessfully");
        }

        closeDialog();

    }

    public void setOnPaymentResultListener(PaymentResultListener resultListener) {
        mPaymentResultListener = resultListener;
    }

    private void closeDialog() {
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public interface PaymentResultListener {
        void onSuccessfullPayment(String paymentType, TransactionDataModel transactionDataModel);

        void onFailedPayment(String message);

        void onCancel();
    }
}
