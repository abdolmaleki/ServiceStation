package com.technotapp.servicestation.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.ProductFactorAdapterModel;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.adapter.FactorAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.AddFactorDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseSto;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.FactorModel;
import com.technotapp.servicestation.enums.PaymentType;
import com.technotapp.servicestation.fragment.IMagCard;
import com.technotapp.servicestation.fragment.PaymentMenuDialog;
import com.technotapp.servicestation.mapper.FactorMapper;
import com.technotapp.servicestation.pax.printer.PrintMaker;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FactorActivity extends BaseActivity implements View.OnClickListener, IPin {


    private FactorModel mFactorModel;
    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;
    @BindView(R.id.activity_factor_tv_totalprice)
    TextView tv_totalPrice;
    @BindView(R.id.activity_factor_list_product)
    ListView list_product;

    private IMagCard mIMagCard;
    private Session mSession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factor);

        loadData();
        initDb();
        initView();
        initAdapter();
    }

    private void initAdapter() {
        try {
            List<ProductFactorAdapterModel> productModels = mFactorModel.getProductModels();
            FactorAdapter adapter = new FactorAdapter(this, productModels);
            list_product.setAdapter(adapter);
        } catch (Exception e) {
            AppMonitor.reportBug(this, e, "FactorActivity", "initAdapter");
        }
    }

    private void initView() {
        ButterKnife.bind(this);
        txtTitle.setText("فاکتور فروش");
        tv_totalPrice.setText(mFactorModel.getTotalPrice() + " ریال");
        back.setOnClickListener(this);
        findViewById(R.id.activity_factor_btn_pay).setOnClickListener(this);
        mSession = Session.getInstance(this);
    }

    private void initDb() {
        Db.init(this);
    }

    private void loadData() {
        long factorId = getIntent().getLongExtra(Constant.Key.FACTOR_ID, -1);
        if (factorId != -1) {
            mFactorModel = Db.Factor.getFactorById(factorId);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == back.getId()) {
            finish();

        } else if (id == R.id.activity_factor_btn_pay) {
            goPaymentMenu();
        }
    }

    private void goPaymentMenu() {
        try {
            if (mFactorModel != null && mFactorModel.getTotalPrice() > 0) {
                PaymentMenuDialog paymentMenuDialog = new PaymentMenuDialog();
                Bundle bundle = new Bundle();
                bundle.putLong(Constant.Key.FACTOR_ID, mFactorModel.getId());
                bundle.putStringArrayList(Constant.Key.PAYMENT_TYPE_LIST, new ArrayList<String>() {{
                    add(PaymentType.CASH);
                    add(PaymentType.EWALLET);
                    add(PaymentType.SHETABI);
                }});
                paymentMenuDialog.setArguments(bundle);
                paymentMenuDialog.show(getSupportFragmentManager(), "payment.menu");
                paymentMenuDialog.setOnPaymentResultListener(new PaymentMenuDialog.PaymentResultListener() {
                    @Override
                    public void onSuccessfullPayment(String paymentType, TransactionDataModel transactionDataModel) {
                        if (paymentType.equals(PaymentType.EWALLET)) {
                            new Handler(getMainLooper()).post(() -> callSubmitFactor(paymentType, transactionDataModel));
                        } else {
                            callSubmitFactor(paymentType, transactionDataModel);
                        }
                    }

                    @Override
                    public void onFailedPayment(String message) {
                        Helper.alert(FactorActivity.this, message, Constant.AlertType.Error);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } else {
                Helper.alert(this, "مبلغی برای پرداخت وجود ندارد", Constant.AlertType.Error);
            }

        } catch (Exception e) {
            AppMonitor.reportBug(this, e, "FactorActivity", "goPaymentMenu");
        }
    }

    private void callSubmitFactor(String paymentType, TransactionDataModel transactionDataModel) {
        try {
            AddFactorDto dto = createAddFactorDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            new ApiCaller(Constant.Api.Type.SUBMIT_FACTOR).call(this, dto, AESsecretKey, "در حال ارسال اطلاعات", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        Type listType = new TypeToken<ArrayList<BaseSto>>() {
                        }.getType();
                        List<BaseSto> sto = gson.fromJson(jsonResult, listType);

                        if (sto != null) {
                            if (sto.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                                mSession.setLastVersion(sto.get(0).messageModel.get(0).ver);
                                Helper.alert(FactorActivity.this, "فاکتور با موفقیت ثبت شد", Constant.AlertType.Success);
                                switch (paymentType) {
                                    case PaymentType.CASH:
                                        PrintMaker.startPrint(FactorActivity.this, Constant.RequestMode.CASH_PAYMENT, mFactorModel.getTotalPrice());
                                        break;
                                    case PaymentType.EWALLET:
                                        PrintMaker.startPrint(FactorActivity.this, Constant.RequestMode.BUY, transactionDataModel);

                                        break;
                                }
                            } else {
                                Helper.alert(FactorActivity.this, sto.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                            }
                        } else {
                            Helper.alert(FactorActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(FactorActivity.this, e, "FactorActivity", "callSubmitFactor");
                        Helper.alert(FactorActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);

                    }
                }

                @Override
                public void onFail(String message) {
                    Helper.alert(FactorActivity.this, message, Constant.AlertType.Error);
                }

            });
        } catch (Exception e) {
            AppMonitor.reportBug(FactorActivity.this, e, "FactorActivity", "callSubmitFactor");
        }
    }

    @Override
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof IMagCard) {
            mIMagCard = (IMagCard) fragment;
        }
    }

    @Override
    public void onPinEntered(String pin) {
        if (mIMagCard != null) {
            mIMagCard.onPinEnteredSuccessfully();
        }
    }

    private AddFactorDto createAddFactorDto() {
        return FactorMapper.convertModelToDto(FactorActivity.this, mFactorModel);
    }

}
