package com.technotapp.servicestation.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.ProductFactorAdapterModel;
import com.technotapp.servicestation.adapter.FactorAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.FactorModel;
import com.technotapp.servicestation.fragment.IMagCard;
import com.technotapp.servicestation.fragment.PaymentMenuDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FactorActivity extends AppCompatActivity implements View.OnClickListener, IPin {


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
            AppMonitor.reportBug(e, "FactorActivity", "initAdapter");
        }
    }

    private void initView() {
        ButterKnife.bind(this);
        txtTitle.setText("فاکتور فروش");
        tv_totalPrice.setText(mFactorModel.getTotalPrice() + "");
        back.setOnClickListener(this);
        findViewById(R.id.activity_factor_btn_pay).setOnClickListener(this);
    }

    private void initDb() {
        Db.init();
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
                paymentMenuDialog.setArguments(bundle);
                paymentMenuDialog.show(getSupportFragmentManager(), "payment.menu");
            } else {
                Helper.alert(this, "مبلغی برای پرداخت وجود ندارد", Constant.AlertType.Error);
            }

        } catch (Exception e) {
            AppMonitor.reportBug(e, "FactorActivity", "goPaymentMenu");
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
}
