package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.activity.CustomQRScannerActivity;
import com.technotapp.servicestation.adapter.DataModel.PaymentTypeAdapterModel;
import com.technotapp.servicestation.adapter.PaymentTypeAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.sto.BaseTransactionSto;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.PaymentModel;
import com.technotapp.servicestation.entity.TransactionService;
import com.technotapp.servicestation.enums.PaymentType;
import com.technotapp.servicestation.mapper.PaymentMapper;
import com.technotapp.servicestation.pax.mag.IMagCardCallback;
import com.technotapp.servicestation.pax.mag.MagCard;
import com.technotapp.servicestation.setting.Session;

import java.util.ArrayList;

import io.realm.RealmResults;

public class PaymentListFragment extends DialogFragment implements View.OnClickListener, PaymentTypeAdapter.PaymentTypeListener {

    private Session mSession;
    private long mAmount;
    private ListView mList_payment;
    private Activity mActivity;
    private boolean mHasCashPayment;
    private PaymentResultListener mPaymentResultListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        initTransaction();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_payment_list, container);
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_btn_swipe_card);
            }
            initView(view);
            initAdapter();
            return view;

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "PaymentMenuDialog", "onCreateView");
            return null;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        resetTransactionService();

    }

    private void resetTransactionService() {
        TransactionService.serviceType = -1;
        TransactionService.dto = null;
    }

    public static PaymentListFragment newInstance() {
        PaymentListFragment fragment = new PaymentListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static PaymentListFragment newInstance(long amount) {
        PaymentListFragment fragment = new PaymentListFragment();
        Bundle args = new Bundle();
        args.putLong(Constant.Key.PAYMENT_AMOUNT, amount);
        fragment.setArguments(args);
        return fragment;
    }

    public static PaymentListFragment newInstance(long amount, boolean hasCashPayment) {
        PaymentListFragment fragment = new PaymentListFragment();
        Bundle args = new Bundle();
        args.putLong(Constant.Key.PAYMENT_AMOUNT, amount);
        args.putBoolean(Constant.Key.HAS_CASH_PAYMENT, hasCashPayment);
        fragment.setArguments(args);
        return fragment;
    }

    private void initTransaction() {
    }

    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mAmount = bundle.getLong(Constant.Key.PAYMENT_AMOUNT, -1);
            mHasCashPayment = bundle.getBoolean(Constant.Key.HAS_CASH_PAYMENT, false);
            TransactionService.amount = mAmount;
        }
    }

    private void initAdapter() {
        RealmResults<PaymentModel> paymentModels = Db.Payment.getAll();
        if (paymentModels == null || paymentModels.size() == 0) {
            Helper.alert(mActivity, "متاسفانه هیچ روش پرداختی وجود ندارد", Constant.AlertType.Error);
            closeDialog();
        } else {
            ArrayList<PaymentTypeAdapterModel> adapterModels = PaymentMapper.convertPaymentModelToAdapter(paymentModels);
            if (mHasCashPayment) {
                PaymentTypeAdapterModel paymentTypeAdapterModel = new PaymentTypeAdapterModel();
                paymentTypeAdapterModel.title = "نقدی";
                paymentTypeAdapterModel.code = PaymentType.CASH;
                adapterModels.add(paymentTypeAdapterModel);
            }
            PaymentTypeAdapter adapter = new PaymentTypeAdapter(mActivity, adapterModels, this);
            mList_payment.setAdapter(adapter);
        }
    }

    private void initView(View view) {
        mList_payment = view.findViewById(R.id.fragment_payment_list_listview);
        mSession = Session.getInstance(mActivity);

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
    }


    private void closeDialog() {
        dismiss();
    }

    @Override
    public void onPaymentClick(String code) {

        TransactionService.paymentType = code;

        switch (code) {
            case PaymentType.SHETABI:
                doShetabiPayment();
                break;
            case PaymentType.WALLET:
                doWalletPayment(null);
                break;
            case PaymentType.QRCode:
                doQRCodePayment();
                break;

            case PaymentType.CASH:
                doCashPayment(mAmount);
                break;
        }

    }


    private void doWalletPayment(String hashId) {

        if (hashId == null) {
            MagCard magCard = MagCard.getInstance();
            magCard.start(mActivity, new IMagCardCallback() {
                @Override
                public void onFail() {

                }

                @Override
                public void onSuccessful(String track1, String track2, String track3) {
                    try {
                        String cardNumber = null;
                        if (track1 != null && !track1.equals("")) {
                            cardNumber = track1.substring(1, 17);
                        } else if (track2 != null && !track2.equals("")) {
                            cardNumber = track2.substring(0, 16);
                        }


                        TransactionService.cardNumber = cardNumber;
                        WalletListDialog walletListDialog = WalletListDialog.newInstance(cardNumber, null, mAmount);
                        walletListDialog.setTargetFragment(PaymentListFragment.this, Constant.RequestCode.WALLET_PAYMENT);
                        walletListDialog.show(getActivity().getSupportFragmentManager(), walletListDialog.getClass().getName());

                    } catch (Exception e) {
                        AppMonitor.reportBug(mActivity, e, "PaymentListFragment", "onSuccessful");
                    }
                }
            });
        } else {
            WalletListDialog walletListDialog = WalletListDialog.newInstance(null, hashId, mAmount);
            walletListDialog.setTargetFragment(PaymentListFragment.this, Constant.RequestCode.WALLET_PAYMENT);
            walletListDialog.show(getActivity().getSupportFragmentManager(), walletListDialog.getClass().getName());
        }
    }

    private void doShetabiPayment() {
        MagCard magCard = MagCard.getInstance();
        magCard.start(mActivity, new IMagCardCallback() {
            @Override
            public void onFail() {
            }

            @Override
            public void onSuccessful(String track1, String track2, String track3) {
            }
        });
    }

    private void doQRCodePayment() {
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
            AppMonitor.reportBug(getActivity(), e, "PaymentListFragment", "doQRCodePayment");
        }
    }

    private void doCashPayment(long amount) {

        try {
            CashPaymentConfirmDialog cashPaymentConfirmDialog = CashPaymentConfirmDialog.newInstance(amount);
            cashPaymentConfirmDialog.show(getActivity().getFragmentManager(), "cashPaymentConfirmDialog");
            cashPaymentConfirmDialog.setmOnCashPaymentDialogClick(new CashPaymentConfirmDialog.OnCashPaymentDialogClick() {
                @Override
                public void onAccept() {
                    BaseTransactionSto sto = new BaseTransactionSto();
                    sto.paymentType = PaymentType.CASH;
                    mPaymentResultListener.onSuccessfullPayment("پرداخت با موفقیت انجام شد", sto);
                    cashPaymentConfirmDialog.dismiss();
                    closeDialog();

                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "PaymentListDialog", "doCashPayment");
        }
    }

    public interface PaymentResultListener {
        void onSuccessfullPayment(String message, BaseTransactionSto response);

        void onFailedPayment(String message, BaseTransactionSto baseTransactionSto);

        void onCancel();
    }

    public void show(FragmentActivity activity, PaymentResultListener resultListener) {
        this.show(activity.getSupportFragmentManager(), "payment.list");
        mPaymentResultListener = resultListener;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.RequestCode.WALLET_PAYMENT:
                    String activePin = data.getStringExtra(Constant.Key.ACTIVE_PIN);
                    String amount = data.getStringExtra(Constant.Key.PAYMENT_AMOUNT);
                    if (activePin != null) {
                        TransactionService.accountPin = activePin;
                    }

                    if (amount != null) {
                        TransactionService.amount = Long.parseLong(amount);
                    }

                    TransactionService.callPaymentService(mActivity, mPaymentResultListener);
                    closeDialog();
                    break;

                case Constant.RequestCode.QR_SCANNER:
                    IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    String codeContent = scanningResult.getContents();
                    String codeFormat = scanningResult.getFormatName();
                    if (codeContent != null && codeFormat != null && !TextUtils.isEmpty(codeContent) && !TextUtils.isEmpty(codeFormat)) {
                        doWalletPayment(codeContent);
                    }
            }
        }
    }

}
