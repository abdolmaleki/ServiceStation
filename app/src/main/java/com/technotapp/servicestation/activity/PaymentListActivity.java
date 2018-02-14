package com.technotapp.servicestation.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.PaymentTypeAdapterModel;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.adapter.PaymentTypeAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.PaymentModel;
import com.technotapp.servicestation.enums.PaymentType;
import com.technotapp.servicestation.fragment.WalletListDialog;
import com.technotapp.servicestation.mapper.PaymentMapper;
import com.technotapp.servicestation.pax.mag.IMagCardCallback;
import com.technotapp.servicestation.pax.mag.MagCard;
import com.technotapp.servicestation.setting.Session;

import java.util.ArrayList;

import io.realm.RealmResults;

public class PaymentListActivity extends BaseActivity implements View.OnClickListener, PaymentTypeAdapter.PaymentTypeListener {

    private Session mSession;
    private TransactionDataModel transactionDataModel;
    private PaymentResultListener mPaymentResultListener;
    private long mAmount;
    private ListView mList_payment;
    private String mPaymentType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payment_list);
        loadData();
        initView();
        initAdapter();
    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAmount = bundle.getLong(Constant.Key.PAYMENT_AMOUNT, -1);
        }
    }

    private void initAdapter() {
        RealmResults<PaymentModel> paymentModels = Db.Payment.getAll();
        if (paymentModels == null || paymentModels.size() == 0) {
            Helper.alert(this, "متاسفانه هیچ روش پرداختی وجود ندارد", Constant.AlertType.Error);
            finish();
        } else {
            ArrayList<PaymentTypeAdapterModel> adapterModels = PaymentMapper.convertPaymentModelToAdapter(paymentModels);
            PaymentTypeAdapter adapter = new PaymentTypeAdapter(this, adapterModels, this);
            mList_payment.setAdapter(adapter);
        }
    }

    private void initView() {

        mSession = Session.getInstance(this);
        mList_payment = findViewById(R.id.activity_payment_list_listview);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    public void setOnPaymentResultListener(PaymentResultListener resultListener) {
        mPaymentResultListener = resultListener;
    }

    private void closeDialog() {
        finish();
    }

    @Override
    public void onPaymentClick(String code) {

        mPaymentType = code;

        switch (code) {
            case PaymentType.SHETABI:
                doShetabiPayment();
                break;
            case PaymentType.WALLET:
                doWalletPayment();
                break;
            case PaymentType.QRCode:
                doQRCodePayment();
                break;
        }

    }

    private void doWalletPayment() {
        MagCard magCard = MagCard.getInstance();
        magCard.start(this, new IMagCardCallback() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccessful(String track1, String track2, String track3) {
                String cardNumber = null;
                if (track1 != null && !track1.equals("")) {
                    cardNumber = track1.substring(1, 17);
                } else if (track2 != null && !track2.equals("")) {
                    cardNumber = track2.substring(0, 16);
                }

                WalletListDialog walletListDialog = WalletListDialog.newInstance(cardNumber, mAmount);
                walletListDialog.show(getSupportFragmentManager(), "wallet.dialog");
                closeDialog();

            }
        });
    }

    private void doShetabiPayment() {
        MagCard magCard = MagCard.getInstance();
        magCard.start(this, new IMagCardCallback() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccessful(String track1, String track2, String track3) {

            }
        });
    }

    private void doQRCodePayment() {
    }

    public interface PaymentResultListener {
        void onSuccessfullPayment(String paymentType, TransactionDataModel transactionDataModel);

        void onFailedPayment(String message);

        void onCancel();
    }


}
