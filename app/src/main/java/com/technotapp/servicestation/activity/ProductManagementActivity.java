package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.ProductManagementAdapter;
import com.technotapp.servicestation.adapter.DataModel.ProductAdapterModel;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.ProductModel;
import com.technotapp.servicestation.fragment.ProductAddEditDialogFragment;
import com.technotapp.servicestation.mapper.ProductMapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductManagementActivity extends AppCompatActivity implements View.OnClickListener, ProductAddEditDialogFragment.ChangeProductsListener, SearchView.OnQueryTextListener {

    @BindView(R.id.activity_product_management_listView)
    ListView productListView;
    @BindView(R.id.activity_product_management_fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;

    private SearchView mSearchView;

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
            mSearchView = findViewById(R.id.activity_product_management_sv_product);
            back.setOnClickListener(this);
            fab.setOnClickListener(this);
            txtTitle.setText("مدیریت کالا ها");

        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductManagementActivity", "initView");
        }

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("جستجو");
    }

    private void initAdapter() {

        try {
            List<ProductModel> productModels = Db.Product.getAll();
            ArrayList<ProductAdapterModel> adapterModels = ProductMapper.convertModelToAdapterModel(productModels);
            ProductManagementAdapter adapter = new ProductManagementAdapter(this, adapterModels);
            productListView.setAdapter(adapter);
            productListView.setTextFilterEnabled(true);

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
        switch (view.getId()) {

            case R.id.toolbar_img_back:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.activity_product_management_fab:
                ProductAddEditDialogFragment dialogFragment = ProductAddEditDialogFragment.newInstance(-1); // -1 means this is a new product
                dialogFragment.show(this);
                break;
        }
    }

    @Override
    public void onAddNewProduct() {
        initAdapter();
    }

    @Override
    public void onUpdateRequest(long productId) {
        ProductAddEditDialogFragment dialogFragment = ProductAddEditDialogFragment.newInstance(productId); // -1 means this is a new product
        dialogFragment.show(this);
    }

    @Override
    public boolean onQueryTextSubmit(String newText) {
        return false;

    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            productListView.clearTextFilter();
        } else {
            productListView.setFilterText(newText);
        }
        return true;
    }


}
