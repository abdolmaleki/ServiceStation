package com.technotapp.servicestation.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.ProductManagementAdapter;
import com.technotapp.servicestation.adapter.DataModel.ProductManagementAdapterModel;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.fragment.ProductAddEditDialogFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductManagementActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.activity_product_management_listView)
    ListView listView;
    @BindView(R.id.activity_product_management_fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);
        ButterKnife.bind(this);
        loadData();

        initDb();

        initView();

        initAdapter();
    }

    private void initView() {
        try {
            back.setOnClickListener(this);
            fab.setOnClickListener(this);
            txtTitle.setText("مدیریت کالا ها");
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductManagementActivity", "initView");
        }
    }

    private void initAdapter() {

        try {
            ProductManagementAdapterModel model1 = new ProductManagementAdapterModel();
            model1.setId("1");
            model1.setName("اصلاح");
            model1.setPrice("25,000");
            model1.setUnit("1");

            ProductManagementAdapterModel model2 = new ProductManagementAdapterModel();
            model2.setId("2");
            model2.setName("شست");
            model2.setPrice("55,000");
            model2.setUnit("2");

            ProductManagementAdapterModel model3 = new ProductManagementAdapterModel();
            model3.setId("3");
            model3.setName("نشست");
            model3.setPrice("29,000");
            model3.setUnit("12");

            ProductManagementAdapterModel model4 = new ProductManagementAdapterModel();
            model4.setId("4");
            model4.setName("خورد");
            model4.setPrice("105,000");
            model4.setUnit("4");
            ProductManagementAdapterModel model5 = new ProductManagementAdapterModel();
            model5.setId("1");
            model5.setName("اصلاح");
            model5.setPrice("25,000");
            model5.setUnit("1");

            ProductManagementAdapterModel model6 = new ProductManagementAdapterModel();
            model6.setId("2");
            model6.setName("شست");
            model6.setPrice("55,000");
            model6.setUnit("2");

            ProductManagementAdapterModel model7 = new ProductManagementAdapterModel();
            model7.setId("3");
            model7.setName("نشست");
            model7.setPrice("29,000");
            model7.setUnit("12");

            ProductManagementAdapterModel model8 = new ProductManagementAdapterModel();
            model8.setId("4");
            model8.setName("خورد");
            model8.setPrice("105,000");
            model8.setUnit("4");

            ArrayList<ProductManagementAdapterModel> adapterModels = new ArrayList<>();
            adapterModels.add(model1);
            adapterModels.add(model2);
            adapterModels.add(model3);
            adapterModels.add(model4);
            adapterModels.add(model5);
            adapterModels.add(model6);
            adapterModels.add(model7);
            adapterModels.add(model8);

            ProductManagementAdapter adapter = new ProductManagementAdapter(this, adapterModels);
            listView.setAdapter(adapter);

        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductManagementFragment", "initAdapter");
        }
    }

    private void loadData() {
    }

    private void initDb() {
        Db.init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.toolbar_img_back:
                finish();
                break;
            case R.id.activity_product_management_fab:
                ProductAddEditDialogFragment dialogFragment =new ProductAddEditDialogFragment();
                dialogFragment.show(this);
                break;

        }
    }
}
